package com.example.domain.email_matcher

interface EmailMatcher {
    fun matches(value: String): Boolean
}