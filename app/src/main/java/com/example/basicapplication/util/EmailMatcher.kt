package com.example.basicapplication.util

interface EmailMatcher {
    fun matches(value: String): Boolean
}