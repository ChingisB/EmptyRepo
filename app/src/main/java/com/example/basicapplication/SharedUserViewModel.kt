package com.example.basicapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicapplication.data.model.User

class SharedUserViewModel: ViewModel() {

    private val _userLiveData = MutableLiveData<User>()
    val userLiveData = _userLiveData


    fun setUser(user: User){
        _userLiveData.postValue(user)
    }
}