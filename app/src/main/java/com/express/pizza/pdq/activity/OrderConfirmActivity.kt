package com.express.pizza.pdq.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.express.pizza.pdq.R
import com.express.pizza.pdq.adapter.OrderItemAdapter
import com.express.pizza.pdq.entity.Pizza
import com.express.pizza.pdq.serialization.CartItem
import kotlinx.android.synthetic.main.activity_order_confirm.*

class OrderConfirmActivity : AppCompatActivity() {

    companion object {
        const val CART_ITEMS = "CART_ITEMS"
    }

    var cartItemMap = LinkedHashMap<Pizza, Int>()
    var amount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirm)
        cartItemMap = (intent.getSerializableExtra(CART_ITEMS) as CartItem).map
        amount = getTotalPrice()
        Log.d("Order--", cartItemMap.size.toString())
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        setSupportActionBar(toolbarOrderConfirm)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
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
    }

    private fun isFirstOrder(): Boolean {
        // todo
        return true
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
        var total = 0.0
        for (entry in cartItemMap) {
            total += entry.key.price?.times(entry.value) ?: 0.0
        }
        return total
    }
}
