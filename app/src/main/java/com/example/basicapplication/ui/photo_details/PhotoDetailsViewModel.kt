package com.example.basicapplication.ui.photo_details


import androidx.lifecycle.*
import com.example.base.BaseViewModel
import com.example.basicapplication.util.Constants
import com.example.domain.entity.PhotoEntity
import com.example.domain.repository.PhotoViewsRepository
import com.example.domain.repository.photo_repository.LocalPhotoRepository
import javax.inject.Inject

class PhotoDetailsViewModel @Inject constructor(
    private val localPhotoRepository: LocalPhotoRepository,
    private val photoViewsRepository: PhotoViewsRepository
) : BaseViewModel() {

    private val _photoSavedState = MutableLiveData<Boolean>()
    val photoSavedState: LiveData<Boolean> = _photoSavedState


    fun savePhoto(photoEntity: PhotoEntity) {
        photoEntity.isSaved = true
        localPhotoRepository.savePhoto(photoEntity).subscribe(
            { _photoSavedState.postValue(true) },
            { _photoSavedState.postValue(false) }
        ).let(compositeDisposable::add)
    }

    fun removePhoto(photoEntity: PhotoEntity) {
        photoEntity.isSaved = false
        localPhotoRepository.removePhoto(photoEntity).subscribe(
            { _photoSavedState.postValue(false) },
            { _photoSavedState.postValue( true) }
        ).let(compositeDisposable::add)
    }

    fun getTotalViews(photoId: Int, callback: (Long) -> Unit) = photoViewsRepository.getPhotoViews(photoId, callback)

    fun viewPhoto(photoEntity: PhotoEntity) = photoViewsRepository.addPhotoView(photoEntity)

}