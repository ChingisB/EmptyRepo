package com.example.basicapplication.ui.signUp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.model.retrofitModel.AuthResponse
import com.example.basicapplication.model.retrofitModel.CreateUser
import com.example.basicapplication.model.retrofitModel.User
import com.example.basicapplication.repository.authenticationRepository.AuthenticationRepository
import com.example.basicapplication.util.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class SignUpViewModel(private val authenticationRepository: AuthenticationRepository<User, CreateUser, AuthResponse>) :
    ViewModel() {


    private val compositeDisposable = CompositeDisposable()


    fun signUp(
        username: String,
        birthday: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        val disposable = authenticationRepository.create(
            CreateUser(
                birthday = birthday,
                username = username,
                password = password,
                confirmPassword = confirmPassword, email = email
            )
        ).subscribeWith(object :
            DisposableSingleObserver<User>() {
            override fun onStart() {
                super.onStart()
            }


            override fun onSuccess(t: User) {
                Log.e("Success", t.toString())
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Log.e("error", e.message.toString())
            }
        })

        compositeDisposable.add(disposable)
    }


    class Factory @Inject constructor(private val authenticationRepository: AuthenticationRepository<User, CreateUser, AuthResponse>) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignUpViewModel(authenticationRepository) as T
            }
            throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
        }
    }
}