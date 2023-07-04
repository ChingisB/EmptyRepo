package com.example.domain.use_case

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ConvertLocalDateUseCase {
    operator fun invoke(date: LocalDate): String = DateTimeFormatter.ISO_DATE.format(date)

}