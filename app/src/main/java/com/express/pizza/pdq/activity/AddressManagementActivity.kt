package com.express.pizza.pdq.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.express.pizza.pdq.R
import com.express.pizza.pdq.adapter.AddressAdapter
import com.express.pizza.pdq.business.entity.Address
import com.express.pizza.pdq.callback.ItemClickListener
import com.express.pizza.pdq.utils.CanClickUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_address_management.*

class AddressManagementActivity : AppCompatActivity(), ItemClickListener {

    companion object {
        const val ADDR_INFO = "ADDR_INFO"
    }

    lateinit var addrList: ArrayList<Address>
    private var fromOrderConfirm = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_management)
        initData()
        initView()
    }

    private fun initData() {
        val listType = object : TypeToken<ArrayList<Address>>() {}.type
        addrList = Gson().fromJson(intent.getStringExtra(ADDR_INFO), listType)
        fromOrderConfirm = intent.getBooleanExtra(OrderConfirmActivity.FROM_ORDER_CONFIRM, false)
    }

    private fun initView() {
        setSupportActionBar(toolbarAddress)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        addrRecyclerView.adapter = AddressAdapter(this, addrList).apply {
            itemClickListener = this@AddressManagementActivity
        }
        addrRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onItemClicked(view: View, pos: Int) {
        if (fromOrderConfirm) {
            setResult(OrderConfirmActivity.ADDRESS_INDEX_RESULT, Intent()
                .apply { putExtra(OrderConfirmActivity.ADDRESS_INDEX, pos) })
            finishAfterTransition()
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        // 防止快速点击启动新Activity
        if (!CanClickUtils.canClick()) {
            return
        }
        super.startActivityForResult(intent, requestCode, options)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
