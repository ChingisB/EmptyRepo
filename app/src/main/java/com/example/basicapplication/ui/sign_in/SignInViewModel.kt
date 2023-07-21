package com.example.basicapplication.ui.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.base.BaseViewModel
import com.example.basicapplication.R
import com.example.basicapplication.util.Constants
import com.example.data.api.model.SignInError
import com.example.data.repository.authentication_repository.AuthenticationRepository
import com.example.domain.resource_provider.ResourceProvider
import com.example.domain.use_case.validation.ValidateEmailUseCase
import com.example.domain.use_case.validation.ValidatePasswordUseCase
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val resourceProvider: ResourceProvider
) : BaseViewModel() {

    private val _signInFormState = MutableLiveData<SignInFormState>()
    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean> = _loggedIn
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

        if (listOf(emailResult, passwordResult).any { !it.success }) {
            formState = formState.copy(emailError = emailResult.errorMessage, passwordError = passwordResult.errorMessage)
            _signInFormState.postValue(formState)
            return
        }
        signIn(formState.email, formState.password)
    }

    private fun signIn(email: String, password: String) {
        authenticationRepository.signIn(email, password).subscribe(
            { _loggedIn.postValue(true) },
            { error ->
                if (error is HttpException && error.code() == 400) handleNetworkError(error)
                _loggedIn.postValue(false)
            })
            .let(compositeDisposable::add)
    }

    private fun handleNetworkError(error: HttpException){
        val body = error.response()?.errorBody()?.string()
        val adapter = Gson().getAdapter(SignInError::class.java)
        val errorResponse = adapter.fromJson(body.toString())
        val message = errorResponse.errorDescription
        val formState = _signInFormState.value ?: SignInFormState()
        if (message.contains("Invalid")) {
            _signInFormState.postValue(
                formState.copy(
                    emailError = resourceProvider.getMessage(R.string.invalid_credentials),
                    passwordError = resourceProvider.getMessage(R.string.invalid_credentials)
                )
            )
        }
    }
}