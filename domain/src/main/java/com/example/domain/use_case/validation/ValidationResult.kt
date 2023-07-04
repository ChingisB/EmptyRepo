package com.example.domain.use_case.validation


data class ValidationResult(val success: Boolean, val errorMessage: String? = null)
