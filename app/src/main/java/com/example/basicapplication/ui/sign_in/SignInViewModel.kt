package com.example.basicapplication.ui.sign_in


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.R
import com.example.basicapplication.data.repository.authentication_repository.AuthenticationRepository
import com.example.basicapplication.domain.use_case.ValidateEmailUseCase
import com.example.basicapplication.domain.use_case.ValidatePasswordUseCase
import com.example.basicapplication.model.retrofit_model.AuthResponse
import com.example.basicapplication.model.retrofit_model.CreateUser
import com.example.basicapplication.model.retrofit_model.User
import com.example.basicapplication.ui.ui_text.UiText
import com.example.basicapplication.util.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class SignInViewModel(
    private val authenticationRepository: AuthenticationRepository<User, CreateUser, AuthResponse>,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase
) :
    ViewModel() {


    private val compositeDisposable = CompositeDisposable()
    private val _signInFormState = MutableLiveData<SignInFormState>()
    private val _loggedIn = MutableLiveData<Resource<Boolean>>()
    val loggedIn: LiveData<Resource<Boolean>>
        get() = _loggedIn
    val signInFormState: LiveData<SignInFormState>
        get() = _signInFormState


    fun onEvent(event: SignInFormEvent) {
        val formState = _signInFormState.value ?: SignInFormState()
        when (event) {
            is SignInFormEvent.EmailChanged -> _signInFormState.postValue(
                formState.copy(
                    email = event.email,
                    emailError = null
                )
            )
            is SignInFormEvent.PasswordChanged -> _signInFormState.postValue(
                formState.copy(
                    password = event.password,
                    passwordError = null
                )
            )
            is SignInFormEvent.Submit -> submit()
        }
    }

    private fun submit() {
        var formState = _signInFormState.value ?: SignInFormState()
        val emailResult = validateEmail(formState.email)
        val passwordResult = validatePassword(formState.password)

        val hasError = listOf(emailResult, passwordResult).any { !it.success }
        if (hasError) {
            formState = formState.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            _signInFormState.postValue(formState)
        } else {
            login(formState.email, formState.password)
        }
    }

    fun login(email: String, password: String) {
        val disposable = authenticationRepository.login(email, password).subscribeWith(object :
            DisposableSingleObserver<AuthResponse>() {
            override fun onStart() {
                super.onStart()
                _loggedIn.postValue(Resource.Loading())
            }

            override fun onSuccess(t: AuthResponse) {
                saveAccessToken(t.accessToken)
                saveRefreshToken(t.refreshToken)
            }

            override fun onError(e: Throwable) {
                _loggedIn.postValue(Resource.Error(data = false, message = e.message.toString()))
                val formState = _signInFormState.value ?: SignInFormState()
                _signInFormState.postValue(
                    formState.copy(
                        emailError = UiText.StringResource(R.string.invalid_credentials),
                        passwordError = UiText.StringResource(R.string.invalid_credentials)
                    )
                )
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

    fun saveRefreshToken(token: String) {
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


    class Factory @Inject constructor(
        private val authenticationRepository: AuthenticationRepository<User, CreateUser, AuthResponse>,
        private val validateEmail: ValidateEmailUseCase,
        private val validatePassword: ValidatePasswordUseCase
    ) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignInViewModel(
                    authenticationRepository,
                    validateEmail,
                    validatePassword
                ) as T
            }
            throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
        }
    }
}