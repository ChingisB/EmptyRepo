package com.example.basicapplication.domain.use_case

import com.example.basicapplication.R
import com.example.basicapplication.ui.ui_text.UiText

class ValidateConfirmPasswordUseCase {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        if (password != confirmPassword) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(R.string.password_match_error)
            )
        }
        return ValidationResult(success = true)
    }
}