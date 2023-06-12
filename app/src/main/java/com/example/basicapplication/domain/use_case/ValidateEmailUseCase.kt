package com.example.basicapplication.domain.use_case

import com.example.basicapplication.R
import com.example.basicapplication.ui.ui_text.UiText
import com.example.basicapplication.util.EmailMatcher
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(private val emailMatcher: EmailMatcher) {
    operator fun invoke(email: String): ValidationResult{
        if(email.isEmpty() || !emailMatcher.matches(email)){
            return ValidationResult(success = false, errorMessage = UiText.StringResource(R.string.invalid_email))
        }
        return ValidationResult(success = true)
    }
}