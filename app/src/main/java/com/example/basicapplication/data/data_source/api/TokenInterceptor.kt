package com.example.basicapplication.data.data_source.api

import com.example.basicapplication.data.repository.token_repository.TokenRepository
import com.example.basicapplication.util.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(private val tokenRepository: TokenRepository): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenRepository.getAccessToken()
        val request = chain.request().newBuilder()
        if(token != null) {
            request.addHeader(Constants.AUTHORIZATION_HEADER, "${Constants.AUTHORIZATION_TYPE} $token")
        }
        return chain.proceed(request.build())
    }
}