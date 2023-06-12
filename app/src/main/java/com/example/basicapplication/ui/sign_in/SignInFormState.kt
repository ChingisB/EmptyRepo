package com.example.basicapplication.ui.sign_in

import com.example.basicapplication.ui.ui_text.UiText

data class SignInFormState(
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null
)