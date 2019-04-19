package com.express.pizza.pdq.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.express.pizza.pdq.R
import com.express.pizza.pdq.adapter.OrderFoodItemAdapter
import com.express.pizza.pdq.business.entity.Address
import com.express.pizza.pdq.business.entity.Order
import com.express.pizza.pdq.business.entity.OrderFoodItem
import com.express.pizza.pdq.utils.UrlConst
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import kotlinx.android.synthetic.main.activity_order_detail.*
import okhttp3.Call
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailActivity : AppCompatActivity() {

    companion object {
        const val ORDER_ID = "ORDER_ID"
        var URL_QUERY_ORDER = UrlConst.URL_PRE + "/order/query"
    }

    private var order: Order? = null
    private var foodList: ArrayList<OrderFoodItem> = ArrayList()
    private var dialogView: View? = null
    private var payDialog: AlertDialog? = null
    private var pendingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbarOrderDetail)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        refreshOrder()
        orderFoodRecyclerView.adapter = OrderFoodItemAdapter(this, foodList)
        orderFoodRecyclerView.layoutManager = LinearLayoutManager(this)
        orderRefreshBtn.setOnClickListener {
            refreshOrder()
            Log.d("order--", "refresh")
        }
    }

    private fun refreshOrder() {
        OkHttpUtils
            .get()
            .url(URL_QUERY_ORDER)
            .addParams("o_id", intent.getStringExtra(ORDER_ID))
            .build()
            .execute(object : StringCallback() {
                @SuppressLint("SetTextI18n")
                override fun onResponse(response: String?, id: Int) {
                    val gson = Gson()
                    val jsonObject = gson.fromJson(response, JsonObject::class.java).asJsonObject
                    if (jsonObject.get("errorCode").asInt == 0) {
                        order = gson.fromJson(jsonObject.get("order").asJsonObject, Order::class.java)
                        order?.apply {
                            val listType = object : TypeToken<ArrayList<OrderFoodItem>>() {}.type
                            foodList = gson.fromJson(this.detail, listType)
                            (orderFoodRecyclerView.adapter as OrderFoodItemAdapter).apply {
                                list = foodList
                                notifyDataSetChanged()
                            }
                            orderStateText.text = when (delivery_state) {
                                0 -> "未支付"
                                1 -> "正在配送"
                                2 -> "配送完成"
                                else -> "未知"
                            }
                            val address = gson.fromJson<Address>(o_delivery_addr, Address::class.java)
                            address?.apply {
                                orderName.text = address.name
                                orderPhone.text = address.phone
                                orderAddress.text = "${address.detail} ${address.note}"
                            }
                            orderPrice.text = "¥" + String.format("%.2f", total_price)
                            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                            orderTime.text =
                                format.format(Calendar.getInstance().apply { timeInMillis = o_create_time }.time)
                            if (delivery_state == 0) {
                                orderPayTime.text = "暂未支付"
                                orderPayBtn.visibility = View.VISIBLE
                                orderPayBtn.setOnClickListener {
                                    showOrderPayDialog()
                                }
                            } else {
                                orderPayBtn.visibility = View.GONE
                                orderPayTime.text =
                                    format.format(Calendar.getInstance().apply { timeInMillis = o_pay_time }.time)
                            }
                            orderID.text = o_id
                            Log.d("order--", order?.u_id)
                        }
                    }
                }

                override fun onError(call: Call?, e: java.lang.Exception?, id: Int) {
                    Toast.makeText(this@OrderDetailActivity, "网络异常，请刷新", Toast.LENGTH_LONG).show()
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun showOrderPayDialog() {
        dialogView = LayoutInflater.from(this).inflate(R.layout.order_pay_dialog, null, false)
        payDialog = AlertDialog.Builder(this).setView(dialogView).create()
        payDialog?.setCanceledOnTouchOutside(false)
        dialogView?.apply {
            findViewById<TextView>(R.id.orderPrice).text = "¥" + String.format("%.2f", order?.total_price)
            findViewById<Button>(R.id.orderPayButton).setOnClickListener {
                it.isEnabled = false
                OkHttpUtils
                    .get()
                    .url(OrderConfirmActivity.URL_PAY_ORDER)
                    .addParams("o_id", order?.o_id)
                    .build()
                    .execute(object : StringCallback() {
                        override fun onResponse(response: String?, id: Int) {
                            val gson = Gson()
                            val jsonObject = gson.fromJson(response, JsonObject::class.java).asJsonObject
                            if (jsonObject.get("errorCode").asInt == 0) {
                                payDialog?.dismiss()
                                refreshOrder()
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
                            Toast.makeText(this@OrderDetailActivity, "网络异常，请再试一次", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
        payDialog?.show()
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
