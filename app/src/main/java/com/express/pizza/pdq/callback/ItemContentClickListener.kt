package com.express.pizza.pdq.callback

import android.view.View
import com.express.pizza.pdq.business.entity.Pizza

interface ItemContentClickListener {
    fun onItemClicked(view: View, pizza: Pizza)
}