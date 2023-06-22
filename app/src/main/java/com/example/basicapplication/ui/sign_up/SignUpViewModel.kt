package com.example.basicapplication.ui.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.R
import com.example.basicapplication.data.repository.authentication_repository.AuthenticationRepository
import com.example.basicapplication.domain.use_case.*
import com.example.basicapplication.data.model.CreateUser
import com.example.basicapplication.data.model.NetworkError
import com.example.basicapplication.ui.ui_text.UiText
import com.example.basicapplication.base.BaseViewModel
import com.example.basicapplication.util.Constants
import com.example.basicapplication.util.Resource
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class SignUpViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val validateUsername: ValidateUsernameUseCase,
    private val validateBirthday: ValidateBirthdayUseCase,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val validateConfirmPassword: ValidateConfirmPasswordUseCase,
    private val convertLocalDate: ConvertLocalDateUseCase,
    private val parseLocalDate: ParseDateUseCase
) :
    BaseViewModel() {

    private val _signedUp = MutableLiveData<Resource<Boolean>>()
    val signedUp: LiveData<Resource<Boolean>> = _signedUp

    private val _signUpFormLiveState = MutableLiveData(SignUpFormState())
    val signUpFormState: LiveData<SignUpFormState> = _signUpFormLiveState


    fun submitEvent(event: SignUpFormEvent){
        onEvent(event)
    }

    private fun onEvent(event: SignUpFormEvent) {
        val formState = signUpFormState.value ?: SignUpFormState()
        when (event) {
            is SignUpFormEvent.UsernameChanged -> _signUpFormLiveState.postValue(
                formState.copy(username = event.username, usernameError = null)
            )
            is SignUpFormEvent.BirthdayChanged -> _signUpFormLiveState.postValue(
                formState.copy(birthday = event.birthday, birthdayError = null)
            )
            is SignUpFormEvent.EmailChanged -> _signUpFormLiveState.postValue(
                formState.copy(email = event.email, emailError = null)
            )
            is SignUpFormEvent.PasswordChanged -> _signUpFormLiveState.postValue(
                formState.copy(password = event.password, passwordError = null)
            )
            is SignUpFormEvent.ConfirmPasswordChanged -> _signUpFormLiveState.postValue(
                formState.copy(confirmPassword = event.confirmPassword, confirmPasswordError = null)
            )
            else -> submitSignUpForm()
        }
    }

    private fun submitSignUpForm() {
        var formState = _signUpFormLiveState.value ?: SignUpFormState()
        val usernameResult = validateUsername.invoke(formState.username)
        val birthdayResult = validateBirthday.invoke(formState.birthday)
        val emailResult = validateEmail.invoke(formState.email)
        val passwordResult = validatePassword.invoke(formState.password)
        val confirmPasswordResult = validateConfirmPassword.invoke(formState.password, formState.confirmPassword)

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
            return
        }
        signUp(
            formState.username,
            convertLocalDate.invoke(parseLocalDate.invoke(formState.birthday)),
            formState.email,
            formState.password,
            formState.confirmPassword
        )
    }

    private fun signUp(
        username: String,
        birthday: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        val createUser =
            CreateUser(birthday = birthday, username = username, password = password, confirmPassword = confirmPassword, email = email)
        authenticationRepository.signUp(createUser).doOnSubscribe { _signedUp.postValue(Resource.Loading()) }
            .subscribe(
                {_signedUp.postValue(Resource.Success(true))},
                {error ->
                    var message: String = error.message.toString()
                    if (error is HttpException) {
                        val body = error.response()?.errorBody()?.string()
                        val adapter = Gson().getAdapter(NetworkError::class.java)
                        val errorResponse = adapter.fromJson(body.toString())
                        message = errorResponse.detail
                        val formState = _signUpFormLiveState.value ?: SignUpFormState()
                        if (message.contains("email")) {
                            _signUpFormLiveState.postValue(formState.copy(emailError = UiText.StringResource(R.string.email_in_use_error)))
                        }
                        if (message.contains("username")) {
                            _signUpFormLiveState.postValue(
                                formState.copy(usernameError = UiText.StringResource(R.string.username_in_use_error))
                            )
                        }
                    }
                    _signedUp.postValue(Resource.Error(message = message))
                }).let(compositeDisposable::add)
    }

    class Factory @Inject constructor(
        private val authenticationRepository: AuthenticationRepository,
        private val validateUsername: ValidateUsernameUseCase,
        private val validateBirthday: ValidateBirthdayUseCase,
        private val validateEmail: ValidateEmailUseCase,
        private val validatePassword: ValidatePasswordUseCase,
        private val validateConfirmPassword: ValidateConfirmPasswordUseCase,
        private val convertLocalDate: ConvertLocalDateUseCase,
        private val parseLocalDate: ParseDateUseCase
    ) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching {
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
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }
}