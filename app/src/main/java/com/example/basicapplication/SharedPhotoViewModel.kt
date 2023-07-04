package com.example.basicapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.base.BaseViewModel
import com.example.domain.entity.PhotoEntity

class SharedPhotoViewModel : BaseViewModel() {

    private val _photoLiveData = MutableLiveData<PhotoEntity>()
    val photoLiveData: LiveData<PhotoEntity> = _photoLiveData

    fun setPhoto(photo: PhotoEntity) {
        _photoLiveData.postValue(photo)
    }
}