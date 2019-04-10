package com.express.pizza.pdq.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    var hasFooter: Boolean = false

    override fun attachLayoutAnimationParameters(
        child: View, params: ViewGroup.LayoutParams,
        index: Int, count: Int
    ) {
        val layoutManager = layoutManager
        if (adapter != null && layoutManager is GridLayoutManager) {
            if (hasFooter && index == count - 1) {
                return
            }
            var animationParams: GridLayoutAnimationController.AnimationParameters? =
                params.layoutAnimationParameters as GridLayoutAnimationController.AnimationParameters?

            if (animationParams == null) {
                animationParams = GridLayoutAnimationController.AnimationParameters()
                params.layoutAnimationParameters = animationParams
            }

            if (!hasFooter) {
                animationParams.count = count
                animationParams.index = index

                val columns = layoutManager.spanCount
                animationParams.columnsCount = columns
                animationParams.rowsCount = count / columns

                val invertedIndex = count - 1 - index
                animationParams.column = columns - 1 - invertedIndex % columns
                animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns
            } else {
                animationParams.count = count - 1
                animationParams.index = index

                val columns = layoutManager.spanCount
                animationParams.columnsCount = columns
                animationParams.rowsCount = count / columns

                val invertedIndex = count - 2 - index
                animationParams.column = columns - 1 - invertedIndex % columns
                animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns
            }

        } else {
            super.attachLayoutAnimationParameters(child, params, index, count)
        }
    }
}
