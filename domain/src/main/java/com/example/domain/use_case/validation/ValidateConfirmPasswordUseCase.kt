package com.example.domain.use_case.validation

import com.example.domain.resource_provider.ResourceProvider

class ValidateConfirmPasswordUseCase(private val resourceProvider: ResourceProvider) {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        if (password != confirmPassword) {
            return ValidationResult(success = false, errorMessage = resourceProvider.getMessage("password_match_error"))
        }
        return ValidationResult(success = true)
    }
}