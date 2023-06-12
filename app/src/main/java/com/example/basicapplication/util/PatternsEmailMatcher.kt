package com.example.basicapplication.util

import android.util.Patterns

class PatternsEmailMatcher: EmailMatcher{
    override fun matches(value: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(value).matches()

}