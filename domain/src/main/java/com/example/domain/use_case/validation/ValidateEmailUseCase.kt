package com.example.domain.use_case.validation

import com.example.domain.email_matcher.EmailMatcher
import com.example.domain.resource_provider.ResourceProvider


class ValidateEmailUseCase(private val emailMatcher: EmailMatcher, private val resourceProvider: ResourceProvider) {
    operator fun invoke(email: String): ValidationResult {
        if(email.isEmpty() || !emailMatcher.matches(email)){
            return ValidationResult(success = false, errorMessage = resourceProvider.getMessage("invalid_email"))
        }
        return ValidationResult(success = true)
    }
}