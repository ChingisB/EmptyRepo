package com.example.basicapplication.ui.profile_settings


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.base.BaseViewModel
import com.example.basicapplication.R
import com.example.basicapplication.util.Constants
import com.example.data.api.model.NetworkError
import com.example.data.repository.AvatarRepository
import com.example.domain.entity.UserEntity
import com.example.domain.repository.user_repository.RemoteUserRepository
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

class ProfileSettingsViewModel @Inject constructor(
    private val remoteUserRepository: RemoteUserRepository,
    private val avatarRepository: AvatarRepository,
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateBirthdayUseCase: ValidateBirthdayUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val resourceProvider: ResourceProvider
) : BaseViewModel() {

    private val _updateUserResult = MutableLiveData<Resource<UserEntity>>()
    val updateUserResult: LiveData<Resource<UserEntity>> = _updateUserResult
    private val _updatePasswordResult = MutableLiveData<Boolean>()
    val updatePasswordResult: LiveData<Boolean> = _updatePasswordResult
    private val _updateAvatarResult = MutableLiveData<Boolean>()
    val updateAvatarResult: LiveData<Boolean> = _updateAvatarResult
    private var initialUserInfoState = ProfileSettingFormState()
    private val _profileSettingsFormState = MutableLiveData<ProfileSettingFormState>()
    val profileSettingFormState: LiveData<ProfileSettingFormState> = _profileSettingsFormState
    var userId = 0
//       TODO unite

    fun setInitialFormState(username: String, birthday: String, email: String) {
        initialUserInfoState = initialUserInfoState.copy(username = username, birthday = birthday, email = email)
        _profileSettingsFormState.postValue(initialUserInfoState)
    }

    fun checkFormErrors(): Boolean {
        val formState = _profileSettingsFormState.value ?: initialUserInfoState
        return listOf(
            formState.birthdayError,
            formState.confirmPasswordError,
            formState.emailError,
            formState.oldPasswordError,
            formState.newPasswordError,
            formState.usernameError,
            formState.avatar
        ).any { it != null }
    }

    fun submitEvent(event: ProfileSettingFormEvent) {
        onEvent(event)
    }

    private fun onEvent(event: ProfileSettingFormEvent) {
        val formState = _profileSettingsFormState.value ?: initialUserInfoState

        when (event) {
            is ProfileSettingFormEvent.EmailChanged -> _profileSettingsFormState.postValue(
                formState.copy(email = event.email, emailError = null)
            )

            is ProfileSettingFormEvent.UsernameChanged -> _profileSettingsFormState.postValue(
                formState.copy(username = event.username, usernameError = null)
            )

            is ProfileSettingFormEvent.BirthdayChanged -> _profileSettingsFormState.postValue(
                formState.copy(birthday = event.birthday, birthdayError = null)
            )

            is ProfileSettingFormEvent.OldPasswordChanged -> _profileSettingsFormState.postValue(
                formState.copy(oldPassword = event.oldPassword, oldPasswordError = null)
            )

            is ProfileSettingFormEvent.NewPasswordChanged -> _profileSettingsFormState.postValue(
                formState.copy(newPassword = event.newPassword, newPasswordError = null)
            )

            is ProfileSettingFormEvent.ConfirmPasswordChanged -> _profileSettingsFormState.postValue(
                formState.copy(confirmPassword = event.confirmPassword, confirmPasswordError = null)
            )

            is ProfileSettingFormEvent.AvatarChanged -> _profileSettingsFormState.postValue(formState.copy(avatar = event.uri))

            else -> submitProfileSettingsForm()
        }
    }

    fun signOut() = signOutUseCase.invoke().subscribe({}, { it.printStackTrace() }).let(compositeDisposable::add)


    private fun checkIsInitialFormState(): Boolean {
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

        val usernameResult = validateUsernameUseCase.invoke(formState.username)
        val birthdayResult = validateBirthdayUseCase.invoke(formState.birthday)
        val emailResult = validateEmailUseCase.invoke(formState.email)
        val oldPasswordResult = validatePasswordUseCase.invoke(formState.oldPassword)
        val newPasswordResult = validatePasswordUseCase.invoke(formState.newPassword)
        val confirmPasswordResult = validateConfirmPasswordUseCase.invoke(formState.newPassword, formState.confirmPassword)

        val isInitialFormState = checkIsInitialFormState()
        if (formState.avatar != null) uploadAvatar(formState.avatar)

        if (isInitialFormState && listOf(oldPassword, newPassword, confirmPassword).all { it.isBlank() || it.isEmpty() }) return

        if (!isInitialFormState && listOf(oldPassword, newPassword, confirmPassword).all { it.isBlank() || it.isEmpty() }) {
            if (listOf(usernameResult, birthdayResult, emailResult).any { !it.success }) {
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

        if (isInitialFormState) {
            if (listOf(oldPasswordResult, newPasswordResult, confirmPasswordResult).any { !it.success }) {
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

        if (listOf(usernameResult, birthdayResult, emailResult, oldPasswordResult, newPasswordResult, confirmPasswordResult)
                .any { !it.success }
        ) {
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
        remoteUserRepository.updatePassword(userId, oldPassword, newPassword).subscribe(
            { _updatePasswordResult.postValue(true) },
            { error ->
                if (error is HttpException) {
                    if (error.code() == 500) _updatePasswordResult.postValue(true)
                    else handleNetworkError(error)
                }
            }
        ).let(compositeDisposable::add)
    }

    private fun updateUserInfo(username: String, email: String, birthday: String) {
        remoteUserRepository.updateUser(userId, username = username, email = email, birthday = birthday).subscribe(
            { _updateUserResult.postValue(Resource.Success(it)) },
            { error -> if (error is HttpException) handleNetworkError(error) }
        ).let(compositeDisposable::add)
    }

    private fun uploadAvatar(imageUri: Uri) {
        if (userId == 0) return
        avatarRepository.uploadAvatar(userId, imageUri).subscribe(
            { _updateAvatarResult.postValue(true) },
            { _updateAvatarResult.postValue(false) }
        ).let(compositeDisposable::add)
    }

    private fun handleNetworkError(error: HttpException) {
        val body = error.response()?.errorBody()?.string()
        val adapter = Gson().getAdapter(NetworkError::class.java)
        val errorResponse = adapter.fromJson(body.toString())
        val message = errorResponse.detail
        val formState = _profileSettingsFormState.value ?: ProfileSettingFormState()
        if (message.contains("email")) {
            _profileSettingsFormState.postValue(formState.copy(emailError = resourceProvider.getMessage(R.string.email_in_use_error)))
        }
        if (message.contains("username")) {
            _profileSettingsFormState.postValue(
                formState.copy(usernameError = resourceProvider.getMessage(R.string.username_in_use_error))
            )
        }
        if (message.contains("oldPassword")) {
            _profileSettingsFormState.postValue(
                formState.copy(oldPasswordError = resourceProvider.getMessage(R.string.incorrect_old_password))
            )
        }
    }

}