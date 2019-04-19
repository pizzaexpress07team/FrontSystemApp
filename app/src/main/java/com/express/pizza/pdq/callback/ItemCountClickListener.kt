package com.express.pizza.pdq.callback

import com.express.pizza.pdq.business.entity.Pizza

interface ItemCountClickListener {
    fun onCountIncreaseClicked(pizza: Pizza, position: Int)

    fun onCountDecreaseClicked(pizza: Pizza, position: Int)
}