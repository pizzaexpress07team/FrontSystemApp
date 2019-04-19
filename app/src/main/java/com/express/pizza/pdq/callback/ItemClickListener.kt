package com.express.pizza.pdq.callback

import android.view.View

interface ItemClickListener {
    fun onItemClicked(view: View, pos: Int)
}