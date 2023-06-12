package com.example.basicapplication.ui.sign_up

sealed class SignUpFormEvent {
    data class UsernameChanged(val username: String): SignUpFormEvent()
    data class BirthdayChanged(val birthday: String): SignUpFormEvent()
    data class EmailChanged(val email: String): SignUpFormEvent()
    data class PasswordChanged(val password: String): SignUpFormEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String): SignUpFormEvent()
    object Submit: SignUpFormEvent()
}