package com.example.basicapplication.ui.sign_in

sealed class SignInFormEvent {
    data class EmailChanged(val email: String): SignInFormEvent()

    data class PasswordChanged(val password: String): SignInFormEvent()

    object Submit: SignInFormEvent()
}