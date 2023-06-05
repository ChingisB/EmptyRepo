package com.example.basicapplication.ui.signIn


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class SignInViewModel(private val authenticationRepository: AuthenticationRepository<User, CreateUser, AuthResponse>) :
    ViewModel() {


    private val compositeDisposable = CompositeDisposable()
    private val _authLiveData = MutableLiveData<Resource<AuthResponse>>()
    private val _loggedIn = MutableLiveData<Resource<Boolean>>()
    val loggedIn: LiveData<Resource<Boolean>>
        get() = _loggedIn
    val authLiveData: LiveData<Resource<AuthResponse>>
        get() = _authLiveData


    fun login(username: String, password: String) {
        val disposable = authenticationRepository.login(username, password).subscribeWith(object :
            DisposableSingleObserver<AuthResponse>() {
            override fun onStart() {
                super.onStart()
                Log.e("started to fetch", "success")
                _authLiveData.postValue(Resource.Loading())
            }

            override fun onSuccess(t: AuthResponse) {
                _authLiveData.postValue(Resource.Success(t))
                Log.e("response!", t.toString())
                saveAccessToken(t.accessToken)
                saveRefreshToken(t.refreshToken)
            }

            override fun onError(e: Throwable) {
                _authLiveData.postValue(Resource.Error(data = null, message = e.message.toString()))
                e.printStackTrace()
            }
        })
        compositeDisposable.add(disposable)
    }

    fun saveAccessToken(token: String) {
        val disposable = authenticationRepository.saveAccessToken(token).subscribe(
            {
                _loggedIn.postValue(Resource.Success(true))
            },
            { error ->
                _loggedIn.postValue(
                    Resource.Error(
                        data = false,
                        message = error.message.toString()
                    )
                )
            })
        compositeDisposable.add(disposable)
    }

    fun saveRefreshToken(token: String){
        val disposable = authenticationRepository.saveRefreshToken(token).subscribe(
            {
                _loggedIn.postValue(Resource.Success(true))
            },
            { error ->
                _loggedIn.postValue(
                    Resource.Error(
                        data = false,
                        message = error.message.toString()
                    )
                )
            })
        compositeDisposable.add(disposable)
    }


    class Factory @Inject constructor(private val authenticationRepositoryImpl: AuthenticationRepository<User, CreateUser, AuthResponse>) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignInViewModel(authenticationRepositoryImpl) as T
            }
            throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
        }
    }
}