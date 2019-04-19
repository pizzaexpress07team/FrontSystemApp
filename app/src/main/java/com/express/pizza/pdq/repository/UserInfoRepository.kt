package com.express.pizza.pdq.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.express.pizza.pdq.business.entity.UserInfo
import com.express.pizza.pdq.utils.UrlConst
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call

class UserInfoRepository {
    companion object {
        var URL_USER_INFO = UrlConst.URL_PRE + "/user/view"
    }

    private var userInfo: MutableLiveData<UserInfo>? = null

    fun fetchUserInfo(uid: String) {
        OkHttpUtils
            .get()
            .url(URL_USER_INFO)
            .addParams("uid", uid)
            .build()
            .execute(object : StringCallback() {
                override fun onResponse(response: String?, id: Int) {
                    val gson = Gson()
                    val jsonObject = gson.fromJson(response, JsonObject::class.java).asJsonObject
                    if (jsonObject.get("errorCode").asInt == 0) {
                        Log.d("User--", jsonObject.get("userInfo").asJsonObject.toString())
                        userInfo?.value = gson.fromJson(jsonObject.get("userInfo").asJsonObject, UserInfo::class.java)
                    }
                }

                override fun onError(call: Call?, e: java.lang.Exception?, id: Int) {
                }
            })
    }

    fun getUserInfo(uid: String): MutableLiveData<UserInfo> {
        if (userInfo == null) {
            userInfo = MutableLiveData()
            fetchUserInfo(uid)
        }
        return userInfo!!
    }
}