package com.express.pizza.pdq.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.express.pizza.pdq.R
import com.express.pizza.pdq.business.entity.Address
import com.express.pizza.pdq.callback.ItemClickListener


class AddressAdapter(val context: Context?, private var list: ArrayList<Address>) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
    companion object {
        private const val TYPE_FOOTER = 1
        private const val TYPE_NORMAL = 0
    }

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
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_address_layout, parent, false)
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
                holder.detail.text = list[position].detail
                holder.note.text = list[position].note
                holder.name.text = list[position].name
                holder.phone.text = list[position].phone
                holder.view.setOnClickListener {
                    itemClickListener?.apply {
                        this.onItemClicked(it, position)
                    }
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var detail: TextView
        lateinit var note: TextView
        lateinit var name: TextView
        lateinit var phone: TextView
        lateinit var view: View

        init {
            if (itemView != footer) {
                view = itemView.findViewById(R.id.view)
                detail = itemView.findViewById(R.id.addrDetail)
                note = itemView.findViewById(R.id.addrNote)
                name = itemView.findViewById(R.id.addrName)
                phone = itemView.findViewById(R.id.addrPhone)
            }
        }
    }
}