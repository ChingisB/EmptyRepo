package com.example.basicapplication.ui.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.base.BaseViewModel
import com.example.basicapplication.util.Constants
import com.example.data.api.model.CreateUser
import com.example.data.api.model.NetworkError
import com.example.data.repository.authentication_repository.AuthenticationRepository
import com.example.domain.resource_provider.ResourceProvider
import com.example.domain.use_case.*
import com.example.domain.use_case.validation.ValidateBirthdayUseCase
import com.example.domain.use_case.validation.ValidateConfirmPasswordUseCase
import com.example.domain.use_case.validation.ValidateEmailUseCase
import com.example.domain.use_case.validation.ValidatePasswordUseCase
import com.example.domain.use_case.validation.ValidateUsernameUseCase
import com.example.util.Resource
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
    private val parseLocalDate: ParseDateUseCase,
    private val resourceProvider: ResourceProvider
) : BaseViewModel() {

    private val _signedUp = MutableLiveData<Resource<Boolean>>()
    val signedUp: LiveData<Resource<Boolean>> = _signedUp
    private val _signUpFormState = MutableLiveData(SignUpFormState())
    val signUpFormState: LiveData<SignUpFormState> = _signUpFormState


    fun submitEvent(event: SignUpFormEvent){
        onEvent(event)
    }

    private fun onEvent(event: SignUpFormEvent) {
        val formState = signUpFormState.value ?: SignUpFormState()
        when (event) {
            is SignUpFormEvent.UsernameChanged -> _signUpFormState.postValue(
                formState.copy(username = event.username, usernameError = null)
            )
            is SignUpFormEvent.BirthdayChanged -> _signUpFormState.postValue(
                formState.copy(birthday = event.birthday, birthdayError = null)
            )
            is SignUpFormEvent.EmailChanged -> _signUpFormState.postValue(
                formState.copy(email = event.email, emailError = null)
            )
            is SignUpFormEvent.PasswordChanged -> _signUpFormState.postValue(
                formState.copy(password = event.password, passwordError = null)
            )
            is SignUpFormEvent.ConfirmPasswordChanged -> _signUpFormState.postValue(
                formState.copy(confirmPassword = event.confirmPassword, confirmPasswordError = null)
            )
            else -> submitSignUpForm()
        }
    }

    private fun submitSignUpForm() {
        var formState = _signUpFormState.value ?: SignUpFormState()
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
            _signUpFormState.postValue(formState)
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
        authenticationRepository.signUp(createUser).doOnSubscribe { _signedUp.postValue(Resource.Loading) }
            .subscribe(
                {_signedUp.postValue(Resource.Success(true))},
                {error ->
                    var message: String = error.message.toString()
                    if (error is HttpException) {
                        val body = error.response()?.errorBody()?.string()
                        val adapter = Gson().getAdapter(NetworkError::class.java)
                        val errorResponse = adapter.fromJson(body.toString())
                        message = errorResponse.detail
                        val formState = _signUpFormState.value ?: SignUpFormState()
                        if (message.contains("email")) {
                            _signUpFormState.postValue(formState.copy(emailError = resourceProvider.getMessage("email_in_use_error")))
                        }
                        if (message.contains("username")) {
                            _signUpFormState.postValue(
                                formState.copy(usernameError = resourceProvider.getMessage("username_in_use_error"))
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
        private val parseLocalDate: ParseDateUseCase,
        private val resourceProvider: ResourceProvider
    ) : ViewModelProvider.Factory {

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
                parseLocalDate,
                resourceProvider
            ) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }
}