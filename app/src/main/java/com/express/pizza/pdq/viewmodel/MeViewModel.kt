package com.express.pizza.pdq.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.express.pizza.pdq.entity.UserInfo
import com.express.pizza.pdq.repository.UserInfoRepository

class MeViewModel : ViewModel() {
    private val uerInfoRepository = UserInfoRepository()
    private var userInfo: MutableLiveData<UserInfo>? = null

    fun getUserInfo(uid: String): MutableLiveData<UserInfo> {
        return uerInfoRepository.getUserInfo(uid)
    }
}
