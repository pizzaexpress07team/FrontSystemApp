package com.express.pizza.pdq.utils

object StringUtils {

    /**
     * 去掉双引号
     */
    fun getJsonString(string: String): String = string.let {
        it.substring(
            1,
            it.length - 1
        )
    }
}