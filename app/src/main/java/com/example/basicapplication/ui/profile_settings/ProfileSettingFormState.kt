package com.example.basicapplication.ui.profile_settings

import com.example.basicapplication.ui.ui_text.UiText

data class ProfileSettingFormState(
    val username: String = "",
    val usernameError: UiText? = null,
    val birthday: String = "",
    val birthdayError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val oldPassword: String = "",
    val oldPasswordError: UiText? = null,
    val newPassword: String = "",
    val newPasswordError: UiText? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: UiText? = null
){
    override fun equals(other: Any?): Boolean {
        if(other !is ProfileSettingFormState){
            return false
        }
        return this.username == other.username && this.email == other.email && this.birthday == other.email
    }
}