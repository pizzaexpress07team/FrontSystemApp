package com.express.pizza.pdq.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.express.pizza.pdq.R
import com.express.pizza.pdq.business.entity.Order
import com.express.pizza.pdq.business.entity.OrderFoodItem
import com.express.pizza.pdq.callback.ItemClickListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*


class OrderAdapter(val context: Context?, var list: ArrayList<Order>) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    companion object {
        private const val TYPE_FOOTER = 1
        private const val TYPE_NORMAL = 0
    }

    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    private val listType = object : TypeToken<ArrayList<OrderFoodItem>>() {}.type
    var footer: View? = null
        set(value) {
            field = value
            notifyItemInserted(itemCount - 1)
        }

    var itemClickListener: ItemClickListener? = null

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
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_order_layout, parent, false)
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
                holder.time.text =
                    format.format(Calendar.getInstance().apply { timeInMillis = list[position].o_create_time }.time)
                holder.price.text = "¥" + String.format("%.2f", list[position].total_price)
                holder.state.text = when (list[position].delivery_state) {
                    0 -> "未支付"
                    1 -> "正在配送"
                    2 -> "配送完成"
                    else -> "未知"
                }
                val foodList: ArrayList<OrderFoodItem> = Gson().fromJson(list[position].detail, listType)
                holder.detail.text =
                    if (foodList.size < 2) foodList[0].name else "${foodList[0].name}、${foodList[1].name}等"
                holder.view.setOnClickListener {
                    itemClickListener?.apply {
                        this.onItemClicked(it, position)
                    }
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var time: TextView
        lateinit var state: TextView
        lateinit var price: TextView
        lateinit var detail: TextView
        lateinit var view: View

        init {
            if (itemView != footer) {
                view = itemView.findViewById(R.id.view)
                time = itemView.findViewById(R.id.orderTime)
                state = itemView.findViewById(R.id.orderState)
                price = itemView.findViewById(R.id.orderPrice)
                detail = itemView.findViewById(R.id.orderDetails)
            }
        }
    }
}