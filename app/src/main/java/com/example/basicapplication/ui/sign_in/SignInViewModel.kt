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
import com.example.basicapplication.util.BaseViewModel
import com.example.basicapplication.util.Constants
import com.example.basicapplication.util.Resource
import javax.inject.Inject

class SignInViewModel(
    private val authenticationRepository: AuthenticationRepository<User, CreateUser, AuthResponse>,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase
) :
    BaseViewModel() {

    private val _signInFormState = MutableLiveData<SignInFormState>()
    private val _loggedIn = MutableLiveData<Resource<Boolean>>()
    val loggedIn: LiveData<Resource<Boolean>> = _loggedIn
    val signInFormState: LiveData<SignInFormState> = _signInFormState


    fun onEvent(event: SignInFormEvent) {
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

//      TODO naming
    private fun submitSignInForm() {
        var formState = _signInFormState.value ?: SignInFormState()
        val emailResult = validateEmail(formState.email)
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
            .doOnSubscribe { _loggedIn.postValue(Resource.Loading()) }
            .subscribe(
                {saveTokens(it)},
                {error ->
                    _loggedIn.postValue(Resource.Error(data = false, message = error.message.toString()))
                    val formState = _signInFormState.value ?: SignInFormState()
                    _signInFormState.postValue(
                        formState.copy(
                            emailError = UiText.StringResource(R.string.invalid_credentials),
                            passwordError = UiText.StringResource(R.string.invalid_credentials)
                        )
                    )
                    error.printStackTrace()})
            .let(compositeDisposable::add)
//  TODO remove object
//                TODO
    }

    private fun saveTokens(authResponse: AuthResponse){
        authenticationRepository.saveTokens(authResponse).subscribe(
            {_loggedIn.postValue(Resource.Success(true))},
            {error -> _loggedIn.postValue(Resource.Error(data = false, message = error.message.toString()))}
        ).let(compositeDisposable::add)
    }

    class Factory @Inject constructor(
        private val authenticationRepository: AuthenticationRepository<User, CreateUser, AuthResponse>,
        private val validateEmail: ValidateEmailUseCase,
        private val validatePassword: ValidatePasswordUseCase
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching{
            @Suppress("UNCHECKED_CAST")
            return SignInViewModel(authenticationRepository, validateEmail, validatePassword) as T
        }.getOrElse { error(Constants.unknownViewModelClassError) }
    }
}