package com.example.basicapplication.ui.profile_settings

import android.net.Uri

sealed class ProfileSettingFormEvent {
    data class AvatarChanged(val uri: Uri?): ProfileSettingFormEvent()
    data class EmailChanged(val email: String): ProfileSettingFormEvent()
    data class UsernameChanged(val username: String): ProfileSettingFormEvent()
    data class BirthdayChanged(val birthday: String): ProfileSettingFormEvent()
    data class OldPasswordChanged(val oldPassword: String): ProfileSettingFormEvent()
    data class NewPasswordChanged(val newPassword: String): ProfileSettingFormEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String): ProfileSettingFormEvent()

    object Submit: ProfileSettingFormEvent()
}