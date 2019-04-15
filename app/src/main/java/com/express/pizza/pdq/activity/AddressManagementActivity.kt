package com.express.pizza.pdq.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.express.pizza.pdq.R
import com.express.pizza.pdq.utils.CanClickUtils
import kotlinx.android.synthetic.main.activity_address_management.*

class AddressManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_management)
        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbarAddress)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
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
