package com.example.basicapplication.model.retrofit_model


import com.example.basicapplication.model.base_model.BaseAuthEntity
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("scope")
    val scope: Any,
    @SerializedName("token_type")
    val tokenType: String
): BaseAuthEntity