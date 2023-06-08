package com.example.basicapplication.ui.sign_up


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.R
import com.example.basicapplication.data.repository.authentication_repository.AuthenticationRepository
import com.example.basicapplication.domain.use_case.*
import com.example.basicapplication.model.retrofit_model.AuthResponse
import com.example.basicapplication.model.retrofit_model.CreateUser
import com.example.basicapplication.model.retrofit_model.SignUpError
import com.example.basicapplication.model.retrofit_model.User
import com.example.basicapplication.ui.ui_text.UiText
import com.example.basicapplication.util.Resource
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.HttpException
import javax.inject.Inject

class SignUpViewModel(
    private val authenticationRepository: AuthenticationRepository<User, CreateUser, AuthResponse>,
    private val validateUsername: ValidateUsernameUseCase,
    private val validateBirthday: ValidateBirthdayUseCase,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val validateConfirmPassword: ValidateConfirmPasswordUseCase,
    private val convertLocalDate: ConvertLocalDateUseCase,
    private val parseLocalDate: ParseDateUseCase
) :
    ViewModel() {


    private val compositeDisposable = CompositeDisposable()
    private val _signedUp = MutableLiveData<Resource<Boolean>>()
    val signedUp: LiveData<Resource<Boolean>>
        get() = _signedUp

    private val _signUpFormLiveState = MutableLiveData(SignUpFormState())
    val signUpFormState: LiveData<SignUpFormState>
        get() = _signUpFormLiveState


    fun onEvent(event: SignUpFormEvent) {
        val formState = signUpFormState.value ?: SignUpFormState()
        when (event) {
            is SignUpFormEvent.UsernameChanged -> _signUpFormLiveState.postValue(
                formState.copy(
                    username = event.username, usernameError = null
                )
            )
            is SignUpFormEvent.BirthdayChanged -> _signUpFormLiveState.postValue(
                formState.copy(
                    birthday = event.birthday, birthdayError = null
                )
            )
            is SignUpFormEvent.EmailChanged -> _signUpFormLiveState.postValue(
                formState.copy(email = event.email, emailError = null)
            )
            is SignUpFormEvent.PasswordChanged -> _signUpFormLiveState.postValue(
                formState.copy(password = event.password, passwordError = null)
            )
            is SignUpFormEvent.ConfirmPasswordChanged -> _signUpFormLiveState.postValue(
                formState.copy(
                    confirmPassword = event.confirmPassword,
                    confirmPasswordError = null
                )
            )
            else -> submit()
        }
    }

    private fun submit() {
        var formState = _signUpFormLiveState.value ?: SignUpFormState()
        val usernameResult = validateUsername(formState.username)
        val birthdayResult = validateBirthday(formState.birthday)
        val emailResult = validateEmail(formState.email)
        val passwordResult = validatePassword(formState.password)
        val confirmPasswordResult =
            validateConfirmPassword(formState.password, formState.confirmPassword)

        val hasError = listOf(
            usernameResult,
            birthdayResult,
            emailResult,
            passwordResult,
            confirmPasswordResult
        ).any { !it.success }

        if (hasError) {
            formState = formState.copy(
                usernameError = usernameResult.errorMessage,
                birthdayError = birthdayResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                confirmPasswordError = confirmPasswordResult.errorMessage
            )
            _signUpFormLiveState.postValue(formState)
        } else {
            signUp(
                formState.username,
                convertLocalDate.invoke(parseLocalDate.invoke(formState.birthday)),
                formState.email,
                formState.password,
                formState.confirmPassword
            )
        }
    }


    private fun signUp(
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
                _signedUp.postValue(Resource.Loading())
            }


            override fun onSuccess(t: User) {
                _signedUp.postValue(Resource.Success(true))
            }

            override fun onError(e: Throwable) {
                var message: String = e.message.toString()
                if (e is HttpException) {
                    val body = e.response()?.errorBody()?.string()
                    val adapter = Gson().getAdapter(SignUpError::class.java)
                    try {
                        val error = adapter.fromJson(body.toString())
                        message = error.detail
                        val formState = _signUpFormLiveState.value ?: SignUpFormState()
                        if (message.contains("email")) {
                            _signUpFormLiveState.postValue(
                                formState.copy(
                                    emailError = UiText.StringResource(
                                        R.string.email_in_use_error
                                    )
                                )
                            )
                        }
                        if (message.contains("username")) {
                            _signUpFormLiveState.postValue(
                                formState.copy(
                                    usernameError = UiText.StringResource(
                                        R.string.username_in_use_error
                                    )
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("Error while parsing value", e.message.toString())
                    }
                }
                _signedUp.postValue(Resource.Error(message = message))
            }
        })

        compositeDisposable.add(disposable)
    }


    class Factory @Inject constructor(
        private val authenticationRepository: AuthenticationRepository<User, CreateUser, AuthResponse>,
        private val validateUsername: ValidateUsernameUseCase,
        private val validateBirthday: ValidateBirthdayUseCase,
        private val validateEmail: ValidateEmailUseCase,
        private val validatePassword: ValidatePasswordUseCase,
        private val validateConfirmPassword: ValidateConfirmPasswordUseCase,
        private val convertLocalDate: ConvertLocalDateUseCase,
        private val parseLocalDate: ParseDateUseCase
    ) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignUpViewModel(
                    authenticationRepository,
                    validateUsername,
                    validateBirthday,
                    validateEmail,
                    validatePassword,
                    validateConfirmPassword,
                    convertLocalDate,
                    parseLocalDate
                ) as T
            }
            throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
        }
    }
}