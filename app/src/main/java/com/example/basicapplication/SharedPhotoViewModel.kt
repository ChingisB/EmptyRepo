package com.example.basicapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.basicapplication.data.model.Photo
import com.example.basicapplication.base.BaseViewModel

class SharedPhotoViewModel : BaseViewModel() {

    private val _photoLiveData = MutableLiveData<Photo>()
    val photoLiveData: LiveData<Photo> = _photoLiveData

    fun setPhoto(photo: Photo) {
        _photoLiveData.postValue(photo)
    }
}