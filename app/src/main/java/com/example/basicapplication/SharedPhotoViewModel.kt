package com.example.basicapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.basicapplication.model.retrofit_model.Photo
import com.example.basicapplication.util.BaseViewModel

class SharedPhotoViewModel: BaseViewModel() {

    private val _photoLiveData = MutableLiveData<Photo>()
    val photoLiveData: LiveData<Photo>
    get() = _photoLiveData

    fun setPhoto(photo: Photo){
        _photoLiveData.postValue(photo)
    }
}