package com.example.basicapplication.ui.make

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.data.repository.photo_repository.PhotoRepository
import com.example.basicapplication.base.BaseViewModel
import com.example.basicapplication.data.model.Photo
import com.example.basicapplication.util.Constants
import com.example.basicapplication.util.Resource
import javax.inject.Inject

class MakeViewModel(private val photoRepository: PhotoRepository): BaseViewModel() {

    private val _imageLiveData = MutableLiveData<Uri>()
    val imageLiveData: LiveData<Uri> = _imageLiveData
    private val _createPhotoResultLiveData = MutableLiveData<Resource<Photo>>()
    val createPhotoResultLiveData: LiveData<Resource<Photo>> = _createPhotoResultLiveData
    var new = false
    var popular = false

    fun setImageUri(uri: Uri){
        _imageLiveData.postValue(uri)
    }

    fun createPhoto(name: String, description: String){
        _imageLiveData.value?.let {imageUri ->
            photoRepository.create(name = name, description = description, new = new, popular = popular, imageUri = imageUri)?.
            doOnSubscribe{_createPhotoResultLiveData.postValue(Resource.Loading())}?.subscribe(
                {_createPhotoResultLiveData.postValue(Resource.Success(it))},
                {_createPhotoResultLiveData.postValue(Resource.Error(message = it.message.toString()))}
            )
        }
    }

    class Factory @Inject constructor(private val photoRepository: PhotoRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return MakeViewModel(photoRepository) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }
}