package com.example.basicapplication

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.base.BaseViewModel
import com.example.basicapplication.ui.photo_details.PhotoDetailsViewModel
import com.example.basicapplication.util.Constants
import com.example.domain.entity.PhotoEntity
import com.example.domain.entity.UserEntity
import com.example.domain.repository.photo_repository.LocalPhotoRepository
import com.example.domain.use_case.GetUserUseCase
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import javax.inject.Inject

class SharedPhotoViewModel(private val getUserUseCase: GetUserUseCase) : BaseViewModel() {

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

    class Factory @Inject constructor(private val getUserUseCase: GetUserUseCase) :
        AbstractSavedStateViewModelFactory() {

        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return SharedPhotoViewModel(getUserUseCase) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }
}