package com.express.pizza.pdq.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.express.pizza.pdq.R
import com.express.pizza.pdq.callback.ItemCountClickListener
import com.express.pizza.pdq.entity.Pizza


class PizzaAdapter(val context: Context?, var list: List<Pizza>) : RecyclerView.Adapter<PizzaAdapter.ViewHolder>() {
    companion object {
        private const val TYPE_FOOTER = 1
        private const val TYPE_NORMAL = 0
    }


    var cartMap: LinkedHashMap<Pizza, Int>? = null

    var itemCountClickListener: ItemCountClickListener? = null

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
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_pizza_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (footer == null) {
            list.size
        } else {
            list.size + 1
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_FOOTER -> return
            else -> {
                holder.name.text = "${list[position].p_name} ${list[position].p_size}'"
                holder.kind.text = list[position].p_type
                holder.price.text = "¥${String.format("%.2f", list[position].price)}"
                Glide.with(context!!)
                    .load(list[position].p_picture)
                    .apply(RequestOptions().placeholder(R.drawable.pizza_item_place_holder))
                    .into(holder.img)

                val count = cartMap?.get(list[position]) ?: 0
                if (count == 0) {
                    holder.removeBtn.visibility = View.GONE
                    holder.count.visibility = View.GONE
                } else {
                    holder.removeBtn.visibility = View.VISIBLE
                    holder.count.apply {
                        visibility = View.VISIBLE
                        text = count.toString()
                    }
                }
                holder.addBtn.setOnClickListener {
                    itemCountClickListener?.apply {
                        this.onCountIncreaseClicked(list[position], position)
                    }
                }
                holder.removeBtn.setOnClickListener {
                    itemCountClickListener?.apply {
                        this.onCountDecreaseClicked(list[position], position)
                    }
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position) == TYPE_FOOTER) manager.spanCount else 1
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var name: TextView
        lateinit var kind: TextView
        lateinit var img: ImageView
        lateinit var price: TextView
        lateinit var addBtn: ImageView
        lateinit var removeBtn: ImageView
        lateinit var count: TextView

        init {
            if (itemView != footer) {
                name = itemView.findViewById(R.id.itemName)
                kind = itemView.findViewById(R.id.itemKind)
                price = itemView.findViewById(R.id.itemPrice)
                img = itemView.findViewById(R.id.itemImg)
                addBtn = itemView.findViewById(R.id.itemAdd)
                removeBtn = itemView.findViewById(R.id.itemRemove)
                count = itemView.findViewById(R.id.itemCount)
            }
        }
    }
}