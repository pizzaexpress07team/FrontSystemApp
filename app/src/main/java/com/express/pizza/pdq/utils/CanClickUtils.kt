package com.express.pizza.pdq.utils

object CanClickUtils {

    private const val TIME_INTERVAL = 1000L
    var lastClickTime = 0L

    fun canClick(): Boolean {
        var flag = false
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > TIME_INTERVAL) {
            flag = true
        }
        lastClickTime = currentTime
        return flag
    }
}