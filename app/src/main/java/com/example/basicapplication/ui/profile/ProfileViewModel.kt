package com.example.basicapplication.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.data.model.Photo
import com.example.basicapplication.data.model.User
import com.example.basicapplication.data.repository.user_repository.UserRepository
import com.example.basicapplication.util.Constants
import com.example.basicapplication.base.PhotoPagingViewModel
import com.example.basicapplication.util.Resource
import io.reactivex.Single
import javax.inject.Inject

class ProfileViewModel(private val userRepository: UserRepository) :
    PhotoPagingViewModel() {

    private val _userLiveData = MutableLiveData<Resource<User>>()
    val userLiveData: LiveData<Resource<User>> = _userLiveData
    var userId = 1


    init {
        if(userLiveData.value == null){
            getUser()
        }
    }

    private fun getUser() {
        userRepository.getCurrentUser().doOnSubscribe{_userLiveData.postValue(Resource.Loading())}.subscribe(
            { _userLiveData.postValue(Resource.Success(it)) },
            { _userLiveData.postValue(Resource.Error(message = it.message.toString())) }
        ).let(compositeDisposable::add)

    }


    class Factory @Inject constructor(private val userRepository: UserRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }

    }

    override fun getDataSource(): Single<List<Photo>> {
        return userRepository.getUserPhotos(userId, page)
    }
}