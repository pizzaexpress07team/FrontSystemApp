package com.express.pizza.pdq.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.express.pizza.pdq.R
import com.express.pizza.pdq.entity.Pizza

class PizzaAdapter(val context: Context?, var list: List<Pizza>) : RecyclerView.Adapter<PizzaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_pizza_layout, null)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position].p_name
        holder.kind.text = list[position].p_type
        holder.price.text = "¥" + String.format("%.2f", list[position].price)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.itemName)
        var kind: TextView = itemView.findViewById(R.id.itemKind)
        var img: ImageView = itemView.findViewById(R.id.itemImg)
        var price: TextView = itemView.findViewById(R.id.itemPrice)
    }
}