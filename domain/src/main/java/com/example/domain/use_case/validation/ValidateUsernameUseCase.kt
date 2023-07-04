package com.example.domain.use_case.validation


import com.example.domain.resource_provider.ResourceProvider


class ValidateUsernameUseCase(private val resourceProvider: ResourceProvider) {
    operator fun invoke(username: String): ValidationResult {
        if (username.isEmpty()) {
            return ValidationResult(success = false, errorMessage = resourceProvider.getMessage("invalid_username"))
        }
        return ValidationResult(success = true)
    }
}