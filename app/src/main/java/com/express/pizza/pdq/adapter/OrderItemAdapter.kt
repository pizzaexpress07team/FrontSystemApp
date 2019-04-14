package com.express.pizza.pdq.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.express.pizza.pdq.R
import com.express.pizza.pdq.entity.Pizza


class OrderItemAdapter(val context: Context?, private var map: LinkedHashMap<Pizza, Int>) :
    RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {
    companion object {
        private const val TYPE_FOOTER = 1
        private const val TYPE_NORMAL = 0
    }

    private var keyList = ArrayList<Pizza>(map.keys)

    var footer: View? = null
        set(value) {
            field = value
            notifyItemInserted(itemCount - 1)
        }

    init {
        keyList.clear()
        keyList.addAll(map.keys)
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
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_cart_order_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (footer == null) {
            map.size
        } else {
            map.size + 1
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_FOOTER -> return
            else -> {
                holder.name.text = keyList[position].p_name
                holder.size.text = "${keyList[position].p_size} 英寸"
                holder.price.text = "¥" + String.format("%.2f", keyList[position].price)
                Glide.with(context!!)
                    .load(keyList[position].p_picture)
                    .apply(RequestOptions().placeholder(R.drawable.pizza_item_place_holder))
                    .into(holder.img)
                holder.count.text = map[keyList[position]].toString()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var name: TextView
        lateinit var size: TextView
        lateinit var img: ImageView
        lateinit var price: TextView
        lateinit var count: TextView

        init {
            if (itemView != footer) {
                name = itemView.findViewById(R.id.itemName)
                size = itemView.findViewById(R.id.itemSize)
                img = itemView.findViewById(R.id.itemImg)
                price = itemView.findViewById(R.id.itemPrice)
                count = itemView.findViewById(R.id.itemCount)
            }
        }
    }
}