package com.example.basicapplication.data.repository.token_repository

interface TokenRepository {
    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    fun saveAccessToken(token: String)

    fun saveRefreshToken(token: String)

    fun saveTokens(accessToken: String, refreshToken: String)

    fun deleteTokens()

    fun deleteAccessToken()

    fun deleteRefreshToken()
}