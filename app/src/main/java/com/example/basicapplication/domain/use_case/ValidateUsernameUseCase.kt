package com.example.basicapplication.domain.use_case

import com.example.basicapplication.R
import com.example.basicapplication.ui.ui_text.UiText

class ValidateUsernameUseCase {
    operator fun invoke(username: String): ValidationResult {
        if (username.isEmpty()) {
            return ValidationResult(success = false, errorMessage = UiText.StringResource(R.string.invalid_username))
        }
        return ValidationResult(success = true)
    }
}