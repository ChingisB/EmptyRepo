package com.example.basicapplication.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.base.PagingViewModel
import com.example.basicapplication.util.Constants
import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.entity.UserEntity
import com.example.domain.use_case.GetCurrentUserUseCase
import com.example.domain.use_case.GetUserPhotosUseCase
import com.example.util.Resource
import javax.inject.Inject

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserPhotosUseCase: GetUserPhotosUseCase
) : PagingViewModel<PaginatedPhotosEntity>() {

    private val _userLiveData = MutableLiveData<Resource<UserEntity>>()
    val userLiveData: LiveData<Resource<UserEntity>> = _userLiveData
    var userId = 1


    init {
        if (userLiveData.value == null) {
            getUser()
        }
    }

    private fun getUser() {
        getCurrentUserUseCase.invoke().doOnSubscribe { _userLiveData.postValue(Resource.Loading) }.subscribe(
            { _userLiveData.postValue(Resource.Success(it)) },
            { _userLiveData.postValue(Resource.Error(message = it.message.toString())) }
        ).let(compositeDisposable::add)

    }

    override fun getDataSource() = getUserPhotosUseCase.invoke(userId, currentPage)

    override fun checkIsLastPage(data: PaginatedPhotosEntity) = data.countOfPages >= currentPage


    class Factory @Inject constructor(
        private val getCurrentUserUseCase: GetCurrentUserUseCase,
        private val getUserPhotosUseCase: GetUserPhotosUseCase
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(getCurrentUserUseCase, getUserPhotosUseCase) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }

    }

}