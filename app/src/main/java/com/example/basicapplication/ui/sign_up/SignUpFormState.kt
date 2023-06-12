package com.example.basicapplication.ui.sign_up

import com.example.basicapplication.ui.ui_text.UiText

data class SignUpFormState(
    val username: String = "",
    val usernameError: UiText? = null,
    val birthday: String = "",
    val birthdayError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: UiText? = null
)
