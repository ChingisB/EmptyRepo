package com.example.basicapplication.ui.photo_details

import android.util.Log
import androidx.lifecycle.*
import com.example.base.BaseViewModel
import com.example.basicapplication.util.Constants
import com.example.domain.entity.PhotoEntity
import com.example.domain.repository.photo_repository.LocalPhotoRepository
import com.example.domain.use_case.GetUserUseCase
import com.example.util.Resource
import javax.inject.Inject

class PhotoDetailsViewModel(
    private val localPhotoRepository: LocalPhotoRepository
) : BaseViewModel() {

    private val _photoSavedState = MutableLiveData<Resource<Boolean>>()
    val photoSavedState: LiveData<Resource<Boolean>> = _photoSavedState

    fun savePhoto(photoEntity: PhotoEntity) {
        photoEntity.isSaved = true
        localPhotoRepository.savePhoto(photoEntity).doOnSubscribe { _photoSavedState.postValue(Resource.Loading) }.subscribe(
            { _photoSavedState.postValue(Resource.Success(true)) },
            { _photoSavedState.postValue(Resource.Error(it.message ?: Constants.SAVING_ERROR)) }
        ).let(compositeDisposable::add)
    }

    fun removePhoto(photoEntity: PhotoEntity) {
        photoEntity.isSaved = false
        localPhotoRepository.removePhoto(photoEntity).doOnSubscribe { _photoSavedState.postValue(Resource.Loading) }.subscribe(
            { _photoSavedState.postValue(Resource.Success(false)) },
            { _photoSavedState.postValue(Resource.Error(it.message ?: Constants.SAVING_ERROR)) }
        ).let(compositeDisposable::add)
    }


    class Factory @Inject constructor(private val localPhotoRepository: LocalPhotoRepository) :
        AbstractSavedStateViewModelFactory() {

        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return PhotoDetailsViewModel(localPhotoRepository) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }
}