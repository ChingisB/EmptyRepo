package com.example.basicapplication.data.data_source.api

import android.content.SharedPreferences
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(private val preferences: SharedPreferences): TokenManager {


    override fun getAccessToken() = preferences.getString("AccessToken", null)

    override fun getRefreshToken() =  preferences.getString("RefreshToken", null)

    override fun saveAccessToken(token: String) = preferences.edit().putString("AccessToken", token).apply()

    override fun saveRefreshToken(token: String) =
        preferences.edit().putString("RefreshToken", token).apply()

    override fun deleteAccessToken() = preferences.edit().remove("AccessToken").apply()

    override fun deleteRefreshToken() = preferences.edit().remove("RefreshToken").apply()

}