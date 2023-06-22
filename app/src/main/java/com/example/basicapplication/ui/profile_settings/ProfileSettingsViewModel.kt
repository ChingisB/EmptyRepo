package com.example.basicapplication.ui.profile_settings


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.R
import com.example.basicapplication.data.model.NetworkError
import com.example.basicapplication.data.model.UpdatePassword
import com.example.basicapplication.data.model.UpdateUser
import com.example.basicapplication.data.repository.token_repository.TokenRepository
import com.example.basicapplication.data.repository.user_repository.UserRepository
import com.example.basicapplication.domain.use_case.*
import com.example.basicapplication.ui.ui_text.UiText
import com.example.basicapplication.base.BaseViewModel
import com.example.basicapplication.util.Constants
import com.example.basicapplication.util.Resource
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class ProfileSettingsViewModel(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    private val validateUsername: ValidateUsernameUseCase,
    private val validateEmail: ValidateEmailUseCase,
    private val validateBirthday: ValidateBirthdayUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val validateConfirmPassword: ValidateConfirmPasswordUseCase
) : BaseViewModel() {


    private val _updateUserResult = MutableLiveData<Resource<Boolean>>()
    val updateUserResult: LiveData<Resource<Boolean>> = _updateUserResult
    private val _updatePasswordResult = MutableLiveData<Resource<Boolean>>()
    val updatePasswordResult: LiveData<Resource<Boolean>> = _updatePasswordResult
    private var initialUserInfoState = ProfileSettingFormState()
    private val _profileSettingsFormState = MutableLiveData<ProfileSettingFormState>()
    val profileSettingFormState: LiveData<ProfileSettingFormState> = _profileSettingsFormState
    var userId = 0

    fun signOut() = tokenRepository.deleteTokens()
//        TODO unite

    fun submitEvent(event: ProfileSettingFormEvent) {
        onEvent(event)
    }

    fun setInitialFormState(username: String, birthday: String, email: String) {
        initialUserInfoState = initialUserInfoState.copy(username = username, birthday = birthday, email = email)
        _profileSettingsFormState.postValue(initialUserInfoState)
        Log.e("formstate", initialUserInfoState.toString())

    }

    private fun onEvent(event: ProfileSettingFormEvent) {
        val formState = _profileSettingsFormState.value ?: initialUserInfoState

        when (event) {
            is ProfileSettingFormEvent.UsernameChanged -> {
                _profileSettingsFormState.postValue(formState.copy(username = event.username, usernameError = null))
            }
            is ProfileSettingFormEvent.BirthdayChanged -> {
                _profileSettingsFormState.postValue(formState.copy(birthday = event.birthday, birthdayError = null))
            }
            is ProfileSettingFormEvent.EmailChanged -> {
                _profileSettingsFormState.postValue(formState.copy(email = event.email, emailError = null))
            }
            is ProfileSettingFormEvent.OldPasswordChanged -> _profileSettingsFormState.postValue(
                formState.copy(oldPassword = event.oldPassword, oldPasswordError = null)
            )
            is ProfileSettingFormEvent.NewPasswordChanged -> _profileSettingsFormState.postValue(
                formState.copy(newPassword = event.newPassword, newPasswordError = null)
            )
            is ProfileSettingFormEvent.ConfirmPasswordChanged -> _profileSettingsFormState.postValue(
                formState.copy(confirmPassword = event.confirmPassword, confirmPasswordError = null)
            )
            else -> submitProfileSettingsForm()
        }
    }

    private fun checkIsInitialFormState(): Boolean{
        val formState = _profileSettingsFormState.value ?: initialUserInfoState
        return formState.email == initialUserInfoState.email && formState.username == initialUserInfoState.username &&
                formState.birthday == initialUserInfoState.birthday
    }

    private fun submitProfileSettingsForm() {
        val formState = _profileSettingsFormState.value ?: initialUserInfoState
        val username = formState.username
        val birthday = formState.birthday
        val email = formState.email
        val oldPassword = formState.oldPassword
        val newPassword = formState.newPassword
        val confirmPassword = formState.confirmPassword


        val usernameResult = validateUsername.invoke(formState.username)
        val birthdayResult = validateBirthday.invoke(formState.birthday)
        val emailResult = validateEmail.invoke(formState.email)
        val oldPasswordResult = validatePassword.invoke(formState.oldPassword)
        val newPasswordResult = validatePassword.invoke(formState.newPassword)
        val confirmPasswordResult = validateConfirmPassword.invoke(formState.newPassword, formState.confirmPassword)

        val isInitialFormState = checkIsInitialFormState()


        if (!isInitialFormState && listOf(oldPassword, newPassword, confirmPassword).all { it == "" }) {
            val hasError = listOf(usernameResult, birthdayResult, emailResult).any { !it.success }
            if (hasError) {
                _profileSettingsFormState.postValue(
                    formState.copy(
                        usernameError = usernameResult.errorMessage,
                        birthdayError = birthdayResult.errorMessage,
                        emailError = birthdayResult.errorMessage
                    )
                )
                return
            }
            updateUserInfo(username, email, birthday)
            return
        }

        if (isInitialFormState && listOf(oldPassword, newPassword, confirmPassword).any { it != "" }) {
            val hasError = listOf(oldPasswordResult, newPasswordResult, confirmPasswordResult).any { !it.success }
            if (hasError) {
                _profileSettingsFormState.postValue(
                    formState.copy(
                        oldPasswordError = oldPasswordResult.errorMessage,
                        newPasswordError = newPasswordResult.errorMessage,
                        confirmPasswordError = confirmPasswordResult.errorMessage
                    )
                )
                return
            }
            updatePassword(oldPassword, newPassword)
            return
        }

        val hasError = listOf(usernameResult, birthdayResult, emailResult, oldPasswordResult, newPasswordResult, confirmPasswordResult)
            .any { !it.success }
        if (hasError) {
            _profileSettingsFormState.postValue(
                formState.copy(
                    usernameError = usernameResult.errorMessage,
                    birthdayError = birthdayResult.errorMessage,
                    emailError = birthdayResult.errorMessage,
                    oldPasswordError = oldPasswordResult.errorMessage,
                    newPasswordError = newPasswordResult.errorMessage,
                    confirmPasswordError = confirmPasswordResult.errorMessage
                )
            )
            return
        }
        updatePassword(oldPassword, newPassword)
        updateUserInfo(username, email, birthday)
    }

    private fun updatePassword(oldPassword: String, newPassword: String) {
        val updatePassword = UpdatePassword(oldPassword, newPassword)
        userRepository.updatePassword(userId, updatePassword).subscribe(
            { _updatePasswordResult.postValue(Resource.Success(true)) },
            { error ->
                val message: String
                if (error is HttpException) {
                    if (error.code() == 500) {
                        _updatePasswordResult.postValue(Resource.Success(true))
                    }
                    val body = error.response()?.errorBody()?.string()
                    val adapter = Gson().getAdapter(NetworkError::class.java)
                    val errorResponse = adapter.fromJson(body.toString())
                    message = errorResponse.detail
                    val formState = _profileSettingsFormState.value ?: ProfileSettingFormState()
                    if (message.contains("oldPassword")) {
                        _profileSettingsFormState.postValue(
                            formState.copy(oldPasswordError = UiText.StringResource(R.string.incorrect_old_password))
                        )
                    }
                }
            }
        ).let(compositeDisposable::add)
    }

    private fun updateUserInfo(username: String, email: String, birthday: String) {
        val updateUser = UpdateUser(username = username, email = email, birthday = birthday)
        userRepository.updateUser(userId, updateUser).doOnSubscribe { _updateUserResult.postValue(Resource.Loading()) }.subscribe(
            { _updateUserResult.postValue(Resource.Success(true)) },
            { error ->
                var message: String = error.message.toString()
                if (error is HttpException) {
                    val body = error.response()?.errorBody()?.string()
                    val adapter = Gson().getAdapter(NetworkError::class.java)
                    val errorResponse = adapter.fromJson(body.toString())
                    message = errorResponse.detail
                    val formState = _profileSettingsFormState.value ?: ProfileSettingFormState()
                    if (message.contains("email")) {
                        _profileSettingsFormState.postValue(formState.copy(emailError = UiText.StringResource(R.string.email_in_use_error)))
                    }
                    if (message.contains("username")) {
                        _profileSettingsFormState.postValue(
                            formState.copy(usernameError = UiText.StringResource(R.string.username_in_use_error))
                        )
                    }
                }
                _updateUserResult.postValue(Resource.Error(message = message))
            }
        ).let(compositeDisposable::add)
    }


    class Factory @Inject constructor(
        private val tokenRepository: TokenRepository,
        private val userRepository: UserRepository,
        private val validateUsername: ValidateUsernameUseCase,
        private val validateEmail: ValidateEmailUseCase,
        private val validateBirthday: ValidateBirthdayUseCase,
        private val validatePassword: ValidatePasswordUseCase,
        private val validateConfirmPassword: ValidateConfirmPasswordUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return ProfileSettingsViewModel(
                tokenRepository,
                userRepository,
                validateUsername,
                validateEmail,
                validateBirthday,
                validatePassword,
                validateConfirmPassword
            ) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }
}