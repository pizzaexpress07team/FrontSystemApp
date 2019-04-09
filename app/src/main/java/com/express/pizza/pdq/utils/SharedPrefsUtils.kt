package com.express.pizza.pdq.utils

import android.content.Context

object SharedPrefsUtils {
    private const val RECORD = "record"

    fun putValue(context: Context?, key: String, value: Int) {
        context?.apply {
            val sp = context.getSharedPreferences(RECORD, Context.MODE_PRIVATE).edit()
            sp.putInt(key, value)
            sp.apply()
        }
    }

    fun putValue(context: Context?, key: String, value: Boolean) {
        context?.apply {
            val sp = context.getSharedPreferences(RECORD, Context.MODE_PRIVATE).edit()
            sp.putBoolean(key, value)
            sp.apply()
        }
    }

    fun putValue(context: Context?, key: String, value: String) {
        context?.apply {
            val sp = context.getSharedPreferences(RECORD, Context.MODE_PRIVATE).edit()
            sp.putString(key, value)
            sp.apply()
        }
    }

    fun getValue(context: Context?, key: String, defValue: Int): Int {
        return context?.let {
            val sp = context.getSharedPreferences(RECORD, Context.MODE_PRIVATE)
            sp.getInt(key, defValue)
        } ?: defValue
    }

    fun getValue(context: Context?, key: String, defValue: Boolean): Boolean {
        return context?.let {
            val sp = context.getSharedPreferences(RECORD, Context.MODE_PRIVATE)
            sp.getBoolean(key, defValue)
        } ?: defValue
    }

    fun getValue(context: Context?, key: String, defValue: String): String {
        return context?.let {
            val sp = context.getSharedPreferences(RECORD, Context.MODE_PRIVATE)
            sp.getString(key, defValue)
        } ?: defValue
    }
}