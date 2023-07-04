package com.example.domain.use_case.validation

import com.example.domain.resource_provider.ResourceProvider
import com.example.domain.use_case.ParseDateUseCase


class ValidateBirthdayUseCase(private val parseDateUseCase: ParseDateUseCase, private val resourceProvider: ResourceProvider) {
    operator fun invoke(birthday: String): ValidationResult {
        return try {
            parseDateUseCase(birthday)
            ValidationResult(success = true)
        } catch (e: Exception) {
            ValidationResult(success = false, errorMessage = resourceProvider.getMessage("invalid_birthday"))
        }
    }

}