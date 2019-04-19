package com.express.pizza.pdq.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.express.pizza.pdq.R
import com.express.pizza.pdq.business.entity.OrderFoodItem


class OrderFoodItemAdapter(val context: Context?, var list: ArrayList<OrderFoodItem>) :
    RecyclerView.Adapter<OrderFoodItemAdapter.ViewHolder>() {
    companion object {
        private const val TYPE_FOOTER = 1
        private const val TYPE_NORMAL = 0
    }

    var footer: View? = null
        set(value) {
            field = value
            notifyItemInserted(itemCount - 1)
        }

    override fun getItemViewType(position: Int): Int {
        return if (footer != null && position == itemCount - 1) {
            TYPE_FOOTER
        } else {
            TYPE_NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            TYPE_FOOTER -> footer?.apply { return ViewHolder(this) }
        }
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_order_food_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_FOOTER -> return
            else -> {
                holder.name.text = "${list[position].name} ${list[position].size}"
//                holder.size.text = "${list[position].size}"
                holder.price.text = "¥" + String.format("%.2f", list[position].price)
                holder.count.text = list[position].num.toString()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var name: TextView
        lateinit var size: TextView
        lateinit var price: TextView
        lateinit var count: TextView

        init {
            if (itemView != footer) {
                name = itemView.findViewById(R.id.itemName)
                size = itemView.findViewById(R.id.itemSize)
                price = itemView.findViewById(R.id.itemPrice)
                count = itemView.findViewById(R.id.itemCount)
            }
        }
    }
}