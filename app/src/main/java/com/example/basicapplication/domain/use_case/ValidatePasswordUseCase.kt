package com.example.basicapplication.domain.use_case


import com.example.basicapplication.R
import com.example.basicapplication.ui.ui_text.UiText

class ValidatePasswordUseCase {
    operator fun invoke(password: String): ValidationResult {
        if (password.isEmpty()) {
            return ValidationResult(success = false, errorMessage = UiText.StringResource(R.string.empty_password_error))
        }
        if (password.length < 5) {
            return ValidationResult(success = false, errorMessage = UiText.StringResource(R.string.password_length_error))
        }
        return ValidationResult(success = true)
    }
}