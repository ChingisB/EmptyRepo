package com.example.basicapplication.ui.photo_details


import androidx.lifecycle.*
import com.example.base.BaseViewModel
import com.example.basicapplication.util.Constants
import com.example.domain.entity.PhotoEntity
import com.example.domain.repository.PhotoViewsRepository
import com.example.domain.repository.photo_repository.LocalPhotoRepository
import com.example.util.Resource
import javax.inject.Inject

class PhotoDetailsViewModel(
    private val localPhotoRepository: LocalPhotoRepository,
    private val photoViewsRepository: PhotoViewsRepository
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

    fun getTotalViews(photoId: Int, callback: (Long) -> Unit) = photoViewsRepository.getPhotoViews(photoId, callback)

    fun viewPhoto(photoEntity: PhotoEntity) = photoViewsRepository.addPhotoView(photoEntity)


    class Factory @Inject constructor(
        private val localPhotoRepository: LocalPhotoRepository,
        private val photoViewsRepository: PhotoViewsRepository
        ) : AbstractSavedStateViewModelFactory() {

        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return PhotoDetailsViewModel(localPhotoRepository, photoViewsRepository) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }
}