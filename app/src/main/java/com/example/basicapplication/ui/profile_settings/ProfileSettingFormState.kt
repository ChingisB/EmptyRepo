package com.example.basicapplication.ui.profile_settings

import android.net.Uri

data class ProfileSettingFormState(
    val avatar: Uri? = null,
    val username: String = "",
    val usernameError: String? = null,
    val birthday: String = "",
    val birthdayError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val oldPassword: String = "",
    val oldPasswordError: String? = null,
    val newPassword: String = "",
    val newPasswordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null
)