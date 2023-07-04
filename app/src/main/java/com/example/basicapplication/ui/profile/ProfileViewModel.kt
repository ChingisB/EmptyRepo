package com.example.basicapplication.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.base.PagingViewModel
import com.example.basicapplication.util.Constants
import com.example.data.repository.FirebaseRepository
import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.entity.UserEntity
import com.example.domain.use_case.GetCurrentUserUseCase
import com.example.domain.use_case.GetUserPhotosUseCase
import com.example.util.Resource
import javax.inject.Inject

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserPhotosUseCase: GetUserPhotosUseCase,
    private val firebaseRepository: FirebaseRepository
) : PagingViewModel<PaginatedPhotosEntity>() {

    private val _userLiveData = MutableLiveData<Resource<UserEntity>>()
    val userLiveData: LiveData<Resource<UserEntity>> = _userLiveData
    private val _avatarLiveData = MutableLiveData<Resource<Uri>>()
    val avatarLiveData: LiveData<Resource<Uri>> = _avatarLiveData
    var userId = 0


    init { if (userLiveData.value == null) { getUser() } }

    private fun getUser() {
        getCurrentUserUseCase.invoke().subscribe(
            { _userLiveData.postValue(Resource.Success(it)) },
            { _userLiveData.postValue(Resource.Error(message = it.message.toString())) }
        ).let(compositeDisposable::add)
    }

    override fun loadPage() { if(userId != 0) super.loadPage() }

    override fun getDataSource() = getUserPhotosUseCase.invoke(userId, currentPage)

    override fun checkIsLastPage(data: PaginatedPhotosEntity) = data.countOfPages >= currentPage

    fun uploadAvatar(imageUri: Uri){
        if(userId == 0) return
        firebaseRepository.uploadAvatar(userId, imageUri).subscribe{ getAvatar() }.let(compositeDisposable::add)
    }

    fun getAvatar(){
        firebaseRepository.getUserAvatar(userId).doOnSubscribe { _avatarLiveData.postValue(Resource.Loading) }.subscribe(
            {_avatarLiveData.postValue(Resource.Success(it))},
            {_avatarLiveData.postValue(Resource.Error(it.message ?: Constants.NETWORK_ERROR))}
        ).let(compositeDisposable::add)
    }


    class Factory @Inject constructor(
        private val getCurrentUserUseCase: GetCurrentUserUseCase,
        private val getUserPhotosUseCase: GetUserPhotosUseCase,
        private val firebaseRepository: FirebaseRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(getCurrentUserUseCase, getUserPhotosUseCase, firebaseRepository) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }

    }

}