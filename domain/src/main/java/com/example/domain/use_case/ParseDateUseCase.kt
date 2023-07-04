package com.example.domain.use_case

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ParseDateUseCase(private val pattern: String) {
    operator fun invoke(date: String): LocalDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern))

}