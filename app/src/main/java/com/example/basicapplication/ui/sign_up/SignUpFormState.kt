package com.example.basicapplication.ui.sign_up


import com.example.util.UiText

data class SignUpFormState(
    val username: String = "",
    val usernameError: String? = null,
    val birthday: String = "",
    val birthdayError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null
)
