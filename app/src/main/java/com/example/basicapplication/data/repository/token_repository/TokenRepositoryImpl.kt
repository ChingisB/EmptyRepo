package com.example.basicapplication.data.repository.token_repository

import android.content.SharedPreferences
import com.example.basicapplication.util.Constants
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(private val preferences: SharedPreferences): TokenRepository {


    override fun getAccessToken() = preferences.getString(Constants.accessTokenKey, null)

    override fun getRefreshToken() =  preferences.getString(Constants.refreshTokenKey, null)

    override fun saveAccessToken(token: String) = preferences.edit().putString(Constants.accessTokenKey, token).apply()

    override fun saveRefreshToken(token: String) = preferences.edit().putString(Constants.refreshTokenKey, token).apply()

    override fun saveTokens(accessToken: String, refreshToken: String) {
        saveAccessToken(accessToken)
        saveRefreshToken(refreshToken)
    }

    override fun deleteTokens() {
        deleteAccessToken()
        deleteRefreshToken()
    }

    override fun deleteAccessToken() = preferences.edit().remove(Constants.accessTokenKey).apply()

    override fun deleteRefreshToken() = preferences.edit().remove(Constants.refreshTokenKey).apply()

}