package com.example.domain.use_case.validation


import com.example.domain.resource_provider.ResourceProvider


class ValidatePasswordUseCase(private val resourceProvider: ResourceProvider) {
    operator fun invoke(password: String): ValidationResult {
        if (password.isEmpty()) {
            return ValidationResult(success = false, errorMessage = resourceProvider.getMessage("empty_password_error"))
        }
        if (password.length < 5) {
            return ValidationResult(success = false, errorMessage = resourceProvider.getMessage("password_length_error"))
        }
        return ValidationResult(success = true)
    }
}