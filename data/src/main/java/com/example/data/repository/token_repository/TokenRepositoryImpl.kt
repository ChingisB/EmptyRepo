package com.example.data.repository.token_repository

import android.content.SharedPreferences
import com.example.data.Constants
import com.example.domain.repository.token_repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(private val preferences: SharedPreferences): TokenRepository {


    override fun getAccessToken() = preferences.getString(Constants.ACCESS_TOKEN_KEY, null)

    override fun getRefreshToken() =  preferences.getString(Constants.REFRESH_TOKEN_KEY, null)

    override fun saveAccessToken(token: String) = preferences.edit().putString(Constants.ACCESS_TOKEN_KEY, token).apply()

    override fun saveRefreshToken(token: String) = preferences.edit().putString(Constants.REFRESH_TOKEN_KEY, token).apply()

    override fun saveTokens(accessToken: String, refreshToken: String) {
        saveAccessToken(accessToken)
        saveRefreshToken(refreshToken)
    }

    override fun deleteTokens() {
        deleteAccessToken()
        deleteRefreshToken()
    }

    override fun deleteAccessToken() = preferences.edit().remove(Constants.ACCESS_TOKEN_KEY).apply()

    override fun deleteRefreshToken() = preferences.edit().remove(Constants.REFRESH_TOKEN_KEY).apply()

}