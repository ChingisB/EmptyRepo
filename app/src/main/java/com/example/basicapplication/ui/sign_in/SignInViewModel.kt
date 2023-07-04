package com.example.basicapplication.ui.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.base.BaseViewModel
import com.example.basicapplication.util.Constants
import com.example.data.api.model.NetworkError
import com.example.data.repository.authentication_repository.AuthenticationRepository
import com.example.domain.resource_provider.ResourceProvider
import com.example.domain.use_case.validation.ValidateEmailUseCase
import com.example.domain.use_case.validation.ValidatePasswordUseCase
import com.example.util.Resource
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class SignInViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val resourceProvider: ResourceProvider
) : BaseViewModel() {

    private val _signInFormState = MutableLiveData<SignInFormState>()
    private val _loggedIn = MutableLiveData<Resource<Boolean>>()
    val loggedIn: LiveData<Resource<Boolean>> = _loggedIn
    val signInFormState: LiveData<SignInFormState> = _signInFormState


    fun submitEvent(event: SignInFormEvent) = onEvent(event)

    private fun onEvent(event: SignInFormEvent) {
        val formState = _signInFormState.value ?: SignInFormState()
        when (event) {
            is SignInFormEvent.EmailChanged -> _signInFormState.postValue(
                formState.copy(email = event.email, emailError = null)
            )
            is SignInFormEvent.PasswordChanged -> _signInFormState.postValue(
                formState.copy(password = event.password, passwordError = null)
            )
            is SignInFormEvent.Submit -> submitSignInForm()
        }
    }

    private fun submitSignInForm() {
        var formState = _signInFormState.value ?: SignInFormState()
        val emailResult = validateEmail.invoke(formState.email)
        val passwordResult = validatePassword(formState.password)

        val hasError = listOf(emailResult, passwordResult).any { !it.success }
        if (hasError) {
            formState = formState.copy(emailError = emailResult.errorMessage, passwordError = passwordResult.errorMessage)
            _signInFormState.postValue(formState)
            return
        }
        signIn(formState.email, formState.password)
    }

    private fun signIn(email: String, password: String) {
        authenticationRepository.signIn(email, password)
            .doOnSubscribe { _loggedIn.postValue(Resource.Loading) }
            .subscribe(
                {value ->
                    authenticationRepository.saveTokens(value)
                    _loggedIn.postValue(Resource.Success(true))},
                {error ->
                    var message: String = error.message.toString()
                    if (error is HttpException && error.code() == 400) {
                        val body = error.response()?.errorBody()?.string()
                        val adapter = Gson().getAdapter(NetworkError::class.java)
                        val errorResponse = adapter.fromJson(body.toString())
                        message = errorResponse.detail
                        val formState = _signInFormState.value ?: SignInFormState()
                        if (message.contains("Invalid")) {
                            _signInFormState.postValue(
                                formState.copy(
                                    emailError = resourceProvider.getMessage("invalid_credentials"),
                                    passwordError = resourceProvider.getMessage("invalid_credentials")
                                )
                            )
                        }
                    }
                    _loggedIn.postValue(Resource.Error(message = message))})
            .let(compositeDisposable::add)
    }

    class Factory @Inject constructor(
        private val authenticationRepository: AuthenticationRepository,
        private val validateEmail: ValidateEmailUseCase,
        private val validatePassword: ValidatePasswordUseCase,
        private val resourceProvider: ResourceProvider
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching{
            @Suppress("UNCHECKED_CAST")
            return SignInViewModel(authenticationRepository, validateEmail, validatePassword, resourceProvider) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }
}