package com.express.pizza.pdq.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.express.pizza.pdq.entity.Pizza
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call

class PizzaRepository() {
    companion object {
        const val URL_MENU_INFO = "http://3.86.76.105:8080/menu/info"
    }

    val pizzaList: LiveData<ArrayList<Pizza>>
        get() {
            val list = MutableLiveData<ArrayList<Pizza>>()
            list.value = null
            OkHttpUtils
                .get()
                .url(URL_MENU_INFO)
                .build()
                .execute(object : StringCallback() {
                    override fun onResponse(response: String?, id: Int) {
                        val gson = Gson()
                        val jsonObject = gson.fromJson(response, JsonObject::class.java).asJsonObject
                        if (jsonObject.get("errorCode").asInt == 0) {
                            val listType = object : TypeToken<ArrayList<Pizza>>() {}.type
                            list.value = gson.fromJson(jsonObject.get("list"), listType)
                            list.value?.addAll(gson.fromJson<ArrayList<Pizza>>(jsonObject.get("list"), listType))
//                            Log.d("Menu--", list.value.toString())
                        }
                    }

                    override fun onError(call: Call?, e: java.lang.Exception?, id: Int) {
                    }
                })
            return list
        }
}