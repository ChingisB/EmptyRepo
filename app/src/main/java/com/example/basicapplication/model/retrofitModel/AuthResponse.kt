package com.example.basicapplication.model.retrofitModel


import com.example.basicapplication.model.baseModel.BaseAuthEntity
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