package com.example.basicapplication.domain.use_case

import com.example.basicapplication.ui.ui_text.UiText

data class ValidationResult(val success: Boolean, val errorMessage: UiText? = null)
