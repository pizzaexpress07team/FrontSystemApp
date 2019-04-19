package com.express.pizza.pdq.repository

import androidx.lifecycle.MutableLiveData
import com.express.pizza.pdq.business.entity.Pizza
import com.express.pizza.pdq.utils.UrlConst
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call

class PizzaRepository {
    companion object {
        var URL_MENU_INFO = UrlConst.URL_PRE + "/menu/info"
    }

    private var pizzaList: MutableLiveData<ArrayList<Pizza>>? = null

    fun fetchPizzaList() {
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
                        pizzaList?.value = gson.fromJson(jsonObject.get("list"), listType)
//                        pizzaList?.value?.addAll(gson.fromJson<ArrayList<Pizza>>(jsonObject.get("list"), listType))
//                            Log.d("Menu--", list.value.toString())
                    }
                }

                override fun onError(call: Call?, e: java.lang.Exception?, id: Int) {
                }
            })
    }

    fun getPizzaList(): MutableLiveData<ArrayList<Pizza>> {
        if (pizzaList == null) {
            pizzaList = MutableLiveData()
            fetchPizzaList()
        }
        return pizzaList!!
    }
}