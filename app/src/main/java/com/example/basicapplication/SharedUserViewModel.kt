package com.example.basicapplication

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.base.BaseViewModel
import com.example.domain.entity.UserEntity

class SharedUserViewModel: BaseViewModel() {

    private val _userLiveData = MutableLiveData<UserEntity>()
    val userLiveData: LiveData<UserEntity> = _userLiveData
    private val _avatarLiveData = MutableLiveData<Uri>()
    val avatarLiveData: LiveData<Uri> = _avatarLiveData


    fun setUser(user: UserEntity) = _userLiveData.postValue(user)

    fun setAvatar(imageUri: Uri) = _avatarLiveData.postValue(imageUri)

}