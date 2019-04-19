package com.express.pizza.pdq.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.express.pizza.pdq.R
import com.express.pizza.pdq.adapter.OrderItemAdapter
import com.express.pizza.pdq.business.entity.Address
import com.express.pizza.pdq.business.entity.Pizza
import com.express.pizza.pdq.business.entity.UserInfo
import com.express.pizza.pdq.serialization.CartItem
import com.express.pizza.pdq.utils.SavedKeyConst
import com.express.pizza.pdq.utils.SharedPrefsUtils
import com.express.pizza.pdq.utils.UrlConst
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import kotlinx.android.synthetic.main.activity_order_confirm.*
import kotlinx.android.synthetic.main.activity_order_detail.view.*
import okhttp3.Call
import okhttp3.MediaType
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

class OrderConfirmActivity : AppCompatActivity() {

    companion object {
        const val CART_ITEMS = "CART_ITEMS"
        const val ADDRESS_INDEX_REQUEST = 100
        const val ADDRESS_INDEX_RESULT = 102
        const val ADDRESS_INDEX = "ADDRESS_INDEX"
        const val FROM_ORDER_CONFIRM = "FROM_ORDER_CONFIRM"
        var URL_CONFIRM_ORDER = UrlConst.URL_PRE + "/order/confirm"
        var URL_PAY_ORDER = UrlConst.URL_PRE + "/order/pay"
    }

    var cartItemMap = LinkedHashMap<Pizza, Int>()
    var addr: String? = null
    private var amount = 0.0
    var shippingCost = 0.0
    var addressIndex = 0
    var orderDetail = StringBuilder("[")
    private var dialogView: View? = null
    private var pendingDialog: AlertDialog? = null
    private var payDialog: AlertDialog? = null
    private var oId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirm)
        cartItemMap = (intent.getSerializableExtra(CART_ITEMS) as CartItem).map
        amount = getTotalPrice()
        initData()
        initView()
    }

    private fun initData() {
        addr = Gson().fromJson(loadUserInfo(), UserInfo::class.java).addr
        // 下单准备
        for (entry in cartItemMap) {
            val sb =
                StringBuilder("{\"name\":\"${entry.key.p_name}\",\"price\":${entry.key.price},\"size\":\"${entry.key.p_size}\",\"num\":${entry.value}},")
            orderDetail.append(sb)
        }
        orderDetail[orderDetail.length - 1] = ']'
        Log.d(
            "order--", "{\"u_id\":\"${SharedPrefsUtils.getValue(this, SavedKeyConst.SAVED_UID, "-1")}\"," +
                    "\"detail\":$orderDetail,\"total_price\":$amount,\"addrID\":$addressIndex}"
        )
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        setSupportActionBar(toolbarOrderConfirm)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val listType = object : TypeToken<ArrayList<Address>>() {}.type
        val addrList: ArrayList<Address> = Gson().fromJson(addr, listType)
        if (addrList.isNotEmpty()) {
            orderAddressText.visibility = View.VISIBLE
            orderNameText.visibility = View.VISIBLE
            orderPhone.visibility = View.VISIBLE
            orderAddressText.text = "${addrList[0].detail} ${addrList[0].note}"
            orderNameText.text = addrList[0].name
            orderPhone.text = addrList[0].phone
            orderSelectAddress.text = "选择其他地址"
        } else {
            orderAddressText.visibility = View.GONE
            orderNameText.visibility = View.GONE
            orderPhone.visibility = View.GONE
            orderSelectAddress.text = "请选择一个收餐地址"
        }

        orderSelectAddress.setOnClickListener {
            val intent = Intent(this@OrderConfirmActivity, AddressManagementActivity::class.java)
            intent.putExtra(AddressManagementActivity.ADDR_INFO, addr)
            intent.putExtra(FROM_ORDER_CONFIRM, true)
            startActivityForResult(intent, ADDRESS_INDEX_REQUEST)
        }


        val adapter = OrderItemAdapter(this, cartItemMap)
        orderCartRecyclerView.adapter = adapter
        orderCartRecyclerView.layoutManager = LinearLayoutManager(this)
        val footer = LayoutInflater.from(this).inflate(R.layout.item_cart_order_footer, orderCartRecyclerView, false)
        if (isFirstOrder()) {
            amount = if (amount - 15 > 0) amount - 15 else 0.01
            footer.findViewById<View>(R.id.firstDiscountView).visibility = View.VISIBLE
        }
        footer.findViewById<TextView>(R.id.totalPrice).text = "¥" + String.format("%.2f", amount)
        adapter.footer = footer
        orderCheckBtn.setOnClickListener {
            showOrderPendingDialog()
            it.isEnabled = false
            OkHttpUtils
                .postString()
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .url(URL_CONFIRM_ORDER)
                .content(
                    "{\"u_id\":\"${SharedPrefsUtils.getValue(this, SavedKeyConst.SAVED_UID, "-1")}\"," +
                            "\"detail\":$orderDetail,\"total_price\":$amount,\"addrID\":$addressIndex}"
                )
                .build()
                .execute(object : StringCallback() {
                    override fun onResponse(response: String?, id: Int) {
                        val gson = Gson()
                        val jsonObject = gson.fromJson(response, JsonObject::class.java).asJsonObject
                        Log.d("order--", jsonObject.get("errorCode").asString)
                        if (jsonObject.get("errorCode").asInt == 0) {
                            Log.d("order--", jsonObject.get("o_id").asString)
                            oId = jsonObject.get("o_id").asString
                            dialogView?.apply {
                                findViewById<View>(R.id.progressBar).visibility = View.INVISIBLE
                                findViewById<View>(R.id.progressOk).visibility = View.VISIBLE
                                findViewById<View>(R.id.progressError).visibility = View.GONE
                                findViewById<TextView>(R.id.orderPendingText).text = "下单成功"
                            }
                            Handler().postDelayed({
                                pendingDialog?.dismiss()
                                showOrderPayDialog()
                            }, 1500)

                        } else if (jsonObject.get("errorCode").asInt == 2) {
                            dialogView?.apply {
                                findViewById<View>(R.id.progressBar).visibility = View.INVISIBLE
                                findViewById<View>(R.id.progressOk).visibility = View.GONE
                                findViewById<View>(R.id.progressError).visibility = View.VISIBLE
                                findViewById<TextView>(R.id.orderPendingText).text = "超出配送范围，下单失败"
                            }
                            Handler().postDelayed({
                                pendingDialog?.dismiss()
                                it.isEnabled = true
                            }, 1500)
                        }
                    }

                    override fun onError(call: Call?, e: java.lang.Exception?, id: Int) {
                        Log.d("order--", "Error")
                        dialogView?.apply {
                            findViewById<View>(R.id.progressBar).visibility = View.INVISIBLE
                            findViewById<View>(R.id.progressOk).visibility = View.GONE
                            findViewById<View>(R.id.progressError).visibility = View.VISIBLE
                            findViewById<TextView>(R.id.orderPendingText).text = "网络异常，下单失败"
                        }
                        Handler().postDelayed({
                            pendingDialog?.dismiss()
                            it.isEnabled = true
                        }, 1500)
                    }
                })
        }
    }

    private fun isFirstOrder(): Boolean {
        // todo
        return false
    }

    private fun showOrderPendingDialog() {
        dialogView = LayoutInflater.from(this).inflate(R.layout.order_pending_dialog, null, false)
        pendingDialog = AlertDialog.Builder(this).setView(dialogView).create()
        pendingDialog?.setCancelable(false)
        pendingDialog?.setCanceledOnTouchOutside(false)
        dialogView?.apply {
            findViewById<View>(R.id.progressBar).visibility = View.VISIBLE
            findViewById<View>(R.id.progressOk).visibility = View.GONE
            findViewById<View>(R.id.progressError).visibility = View.GONE
            findViewById<TextView>(R.id.orderPendingText).text = "订单确认中"
        }
        pendingDialog?.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showOrderPayDialog() {
        dialogView = LayoutInflater.from(this).inflate(R.layout.order_pay_dialog, null, false)
        payDialog = AlertDialog.Builder(this).setView(dialogView).create()
        payDialog?.setCancelable(false)
        payDialog?.setCanceledOnTouchOutside(false)
        dialogView?.apply {
            findViewById<TextView>(R.id.orderPrice).text = "¥" + String.format("%.2f", amount)
            findViewById<Button>(R.id.orderPayButton).setOnClickListener {
                it.isEnabled = false
                OkHttpUtils
                    .get()
                    .url(URL_PAY_ORDER)
                    .addParams("o_id", oId)
                    .build()
                    .execute(object : StringCallback() {
                        override fun onResponse(response: String?, id: Int) {
                            val gson = Gson()
                            val jsonObject = gson.fromJson(response, JsonObject::class.java).asJsonObject
                            if (jsonObject.get("errorCode").asInt == 0) {
                                Intent(this@OrderConfirmActivity, OrderDetailActivity::class.java)
                                    .putExtra(OrderDetailActivity.ORDER_ID, oId)
                                    .apply { startActivity(this) }
                                this@OrderConfirmActivity.finish()
                            } else if (jsonObject.get("errorCode").asInt == 2) {
                                payDialog?.dismiss()
                                showOrderPendingDialog()
                                dialogView?.apply {
                                    findViewById<View>(R.id.progressBar).visibility = View.INVISIBLE
                                    findViewById<View>(R.id.progressOk).visibility = View.GONE
                                    findViewById<View>(R.id.progressError).visibility = View.VISIBLE
                                    findViewById<TextView>(R.id.orderPendingText).text = "超出配送范围，下单失败"
                                }
                                Handler().postDelayed({
                                    pendingDialog?.dismiss()
                                    it.isEnabled = true
                                }, 1500)
                            }
                        }

                        override fun onError(call: Call?, e: java.lang.Exception?, id: Int) {
                            it.isEnabled = true
                            Toast.makeText(this@OrderConfirmActivity, "请再试一次", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
        payDialog?.show()
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ADDRESS_INDEX_RESULT) {
            if (requestCode == ADDRESS_INDEX_REQUEST) {
                data?.let {
                    val index = it.getIntExtra(ADDRESS_INDEX, 0)
                    val listType = object : TypeToken<ArrayList<Address>>() {}.type
                    val addrList: ArrayList<Address> = Gson().fromJson(addr, listType)
                    orderAddressText.text = "${addrList[index].detail} ${addrList[index].note}"
                    orderNameText.text = addrList[index].name
                    orderPhone.text = addrList[index].phone
                    addressIndex = index
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getTotalPrice(): Double {
        var total = shippingCost
        for (entry in cartItemMap) {
            total += entry.key.price?.times(entry.value) ?: 0.0
        }
        return total
    }

    private fun loadUserInfo(): String {
        var reader: BufferedReader? = null
        val content = StringBuilder()
        try {
            val `in` = openFileInput("user_info")
            reader = BufferedReader(InputStreamReader(`in`))
            var line = reader.readLine()
            while (line != null) {
                content.append(line)
                line = reader.readLine()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return content.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        pendingDialog?.apply {
            dismiss()
        }
        payDialog?.apply {
            dismiss()
        }
    }
}
