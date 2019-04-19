package com.express.pizza.pdq.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.express.pizza.pdq.R
import kotlinx.android.synthetic.main.activity_item_details.*

class ItemDetailsActivity : AppCompatActivity() {

    companion object {
        const val DETAILS_IMG = "details_img"
        const val DETAILS_NAME = "details_name"
        const val DETAILS_SIZE = "details_size"
        const val DETAILS_PRICE = "details_price"
        const val DETAILS_RESOURCE = "details_resource"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
//        val slide = Slide(Gravity.BOTTOM)
//        slide.excludeTarget(android.R.id.navigationBarBackground, true)
//        slide.excludeTarget(android.R.id.statusBarBackground, true)
//        slide.excludeTarget(android.R.id.home, true)
//        slide.addTarget(itemName)
//        slide.addTarget(itemPrice)
//        slide.addTarget(itemSize)
//        slide.addTarget(itemResource)
//        window.enterTransition = slide

        setSupportActionBar(toolbarDetails)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val intent = intent
        val imgUrl = intent.getStringExtra(DETAILS_IMG)
        Glide.with(this)
            .load(imgUrl)
            .apply(RequestOptions().placeholder(R.drawable.pizza_item_place_holder))
            .into(itemImg)

        itemName.text = intent.getStringExtra(DETAILS_NAME)
        itemPrice.text = "¥${String.format("%.2f", intent.getDoubleExtra(DETAILS_PRICE, 0.0))}"
        itemSize.text = "规格：" + intent.getStringExtra(DETAILS_SIZE)
        val resource = intent.getStringExtra(DETAILS_RESOURCE)
        itemResource.text = "原料：" + resource.subSequence(1, resource.length - 1)
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
}
