package com.example.basicapplication.data.data_source.api

interface TokenManager {
    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    fun saveAccessToken(token: String)

    fun saveRefreshToken(token: String)

    fun deleteAccessToken()

    fun deleteRefreshToken()
}