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


class CartItemAdapter(val context: Context?, private var map: LinkedHashMap<Pizza, Int>) :
    RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {
//    companion object {
//        private const val TYPE_FOOTER = 1
//        private const val TYPE_NORMAL = 0
//    }

    var countClickListener: CountClickListener? = null
    var keyList = ArrayList<Pizza>(map.keys)

//    var footer: View? = null
//        set(value) {
//            field = value
//            notifyItemInserted(itemCount - 1)
//        }

//    override fun getItemViewType(position: Int): Int {
//        return if (footer != null && position == itemCount - 1) {
//            TYPE_FOOTER
//        } else {
//            TYPE_NORMAL
//        }
//    }

    fun setMap(map: LinkedHashMap<Pizza, Int>) {
        this.map = map
        keyList.clear()
        keyList.addAll(map.keys)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        when (viewType) {
//            TYPE_FOOTER -> footer?.apply { return ViewHolder(this) }
//        }
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_cart_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return map.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = keyList[position].p_name
        holder.size.text = "${keyList[position].p_size} 英寸"
        holder.price.text = "¥" + String.format("%.2f", keyList[position].price)
        Glide.with(context!!)
            .load(keyList[position].p_picture)
            .apply(RequestOptions().placeholder(R.drawable.pizza_item_place_holder))
            .into(holder.img)
        holder.count.text = map[keyList[position]].toString()
        holder.addBtn.setOnClickListener {
            countClickListener?.apply {
                this.onCountIncreaseClicked(keyList[position], position)
            }
        }
        holder.removeBtn.setOnClickListener {
            countClickListener?.apply {
                this.onCountDecreaseClicked(keyList[position], position)
            }
        }
    }

    fun deleteItem(position: Int) {
        notifyItemRemoved(position)
        if (position != map.size) {
            notifyItemRangeChanged(position, map.size - position)
        }
    }

//    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
//        super.onAttachedToRecyclerView(recyclerView)
//        val manager = recyclerView.layoutManager
//        if (manager is GridLayoutManager) {
//            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                override fun getSpanSize(position: Int): Int {
//                    return if (getItemViewType(position) == TYPE_FOOTER) manager.spanCount else 1
//                }
//
//            }
//        }
//    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.itemName)
        var size: TextView = itemView.findViewById(R.id.itemSize)
        var img: ImageView = itemView.findViewById(R.id.itemImg)
        var price: TextView = itemView.findViewById(R.id.itemPrice)
        var count: TextView = itemView.findViewById(R.id.itemCount)
        var addBtn: ImageView = itemView.findViewById(R.id.itemAdd)
        var removeBtn: ImageView = itemView.findViewById(R.id.itemRemove)
    }

    interface CountClickListener {
        fun onCountIncreaseClicked(pizza: Pizza, position: Int)

        fun onCountDecreaseClicked(pizza: Pizza, position: Int)
    }
}