package com.example.basicapplication.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.data.repository.user_repository.UserRepository
import com.example.basicapplication.model.retrofit_model.UpdatePassword
import com.example.basicapplication.model.retrofit_model.UpdateUser
import com.example.basicapplication.model.retrofit_model.User
import com.example.basicapplication.util.BaseViewModel
import com.example.basicapplication.util.Constants
import javax.inject.Inject

class ProfileViewModel(private val userRepository: UserRepository<User, UpdateUser, UpdatePassword>) :
    BaseViewModel() {

    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User>
        get() = _userLiveData


    init {
        getUser()
    }

    private fun getUser() {
        val disposable = userRepository.getCurrentUser()
            .subscribe({ value -> _userLiveData.postValue(value) },
                { error -> error.printStackTrace() })

        compositeDisposable.add(disposable)
    }

    class Factory @Inject constructor(private val userRepository: UserRepository<User, UpdateUser, UpdatePassword>) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository) as T
        }.getOrElse { error(Constants.unknownViewModelClassError) }

    }

}