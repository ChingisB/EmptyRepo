package com.example.basicapplication.domain.use_case

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ParseDateUseCase(private val pattern: String) {
    operator fun invoke(date: String): LocalDate{
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern))
    }
}