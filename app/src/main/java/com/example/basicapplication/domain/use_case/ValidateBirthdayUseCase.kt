package com.example.basicapplication.domain.use_case

import com.example.basicapplication.R
import com.example.basicapplication.ui.ui_text.UiText

class ValidateBirthdayUseCase(private val parseDateUseCase: ParseDateUseCase) {
    operator fun invoke(birthday: String): ValidationResult {
        return try {
            parseDateUseCase(birthday)
            ValidationResult(success = true)
        } catch (e: Exception) {
            ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(R.string.invalid_birthday)
            )
        }
    }

}