package com.example.basicapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.base.BaseViewModel
import com.example.domain.entity.PhotoEntity
import com.example.domain.entity.UserEntity
import com.example.domain.use_case.GetUserUseCase
import javax.inject.Inject

class SharedPhotoViewModel @Inject constructor(private val getUserUseCase: GetUserUseCase) : BaseViewModel() {

    private val _photoLiveData = MutableLiveData<PhotoEntity>()
    val photoLiveData: LiveData<PhotoEntity> = _photoLiveData
    private val _userLiveData = MutableLiveData<UserEntity>()
    val userLiveData: LiveData<UserEntity> = _userLiveData

    fun setPhoto(photo: PhotoEntity) {
        _photoLiveData.postValue(photo)
        getUser(photo)
    }

    private fun getUser(photo: PhotoEntity) =
        getUserUseCase.invoke(photo.userID).subscribe({_userLiveData.postValue(it)}, {it.printStackTrace()}).let(compositeDisposable::add)

}