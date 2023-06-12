package com.example.basicapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicapplication.model.retrofit_model.Photo

class SharedPhotoViewModel: ViewModel() {

    private val _photoLiveData = MutableLiveData<Photo>()
    val photoLiveData: LiveData<Photo>
    get() = _photoLiveData

    fun setPhoto(photo: Photo){
        _photoLiveData.postValue(photo)
    }
}