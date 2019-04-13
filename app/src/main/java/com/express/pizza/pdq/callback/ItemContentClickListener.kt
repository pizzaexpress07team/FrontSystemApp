package com.express.pizza.pdq.callback

import com.express.pizza.pdq.entity.Pizza

interface ItemContentClickListener {
    fun onItemClicked(pizza: Pizza)
}