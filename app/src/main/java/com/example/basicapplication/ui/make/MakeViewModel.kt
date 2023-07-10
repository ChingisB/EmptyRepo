package com.example.basicapplication.ui.make


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.base.BaseViewModel
import com.example.basicapplication.util.Constants
import com.example.domain.entity.PhotoEntity
import com.example.domain.repository.photo_repository.RemotePhotoRepository
import com.example.util.Resource
import java.io.File
import javax.inject.Inject

class MakeViewModel(private val remotePhotoRepository: RemotePhotoRepository) : BaseViewModel() {

    private val _createPhotoResultLiveData = MutableLiveData<Resource<PhotoEntity>>()
    val createPhotoResultLiveData: LiveData<Resource<PhotoEntity>> = _createPhotoResultLiveData
    var new = false
    var popular = false


    fun createPhoto(imageFile: File, name: String, description: String) {
        remotePhotoRepository.create(name = name, description = description, new = new, popular = popular, imageFile = imageFile)
            .doOnSubscribe { _createPhotoResultLiveData.postValue(Resource.Loading) }.subscribe(
            { _createPhotoResultLiveData.postValue(Resource.Success(it)) },
            { _createPhotoResultLiveData.postValue(Resource.Error(message = it.message.toString())) }
        ).let(compositeDisposable::add)
    }

    class Factory @Inject constructor(private val remotePhotoRepository: RemotePhotoRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return MakeViewModel(remotePhotoRepository) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }
}