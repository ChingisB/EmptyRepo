package com.example.basicapplication.api

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(private val preferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        return chain.proceed(requestBuilder.build())
    }

    private fun getToken(): String? {
        return try {
            preferences.getString("AccessToken", null)
        } catch (e: Exception) {
            null
        }
    }
}
