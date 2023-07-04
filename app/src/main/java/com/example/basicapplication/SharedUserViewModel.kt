package com.example.basicapplication

import androidx.lifecycle.MutableLiveData
import com.example.base.BaseViewModel
import com.example.domain.entity.UserEntity

class SharedUserViewModel: BaseViewModel() {

    private val _userLiveData = MutableLiveData<UserEntity>()
    val userLiveData = _userLiveData


    fun setUser(user: UserEntity){
        _userLiveData.postValue(user)
    }
}