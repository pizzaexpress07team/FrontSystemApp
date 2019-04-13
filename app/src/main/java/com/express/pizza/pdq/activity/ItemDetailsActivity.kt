package com.express.pizza.pdq.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.express.pizza.pdq.R
import kotlinx.android.synthetic.main.activity_item_details.*

class ItemDetailsActivity : AppCompatActivity() {

    companion object {
        const val DETAILS_IMG = "details_img"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)
        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbarDetails)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val imgUrl = intent.getStringExtra(DETAILS_IMG)
        Glide.with(this)
            .load(imgUrl)
            .apply(RequestOptions().placeholder(R.drawable.pizza_item_place_holder))
            .into(itemImg)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
