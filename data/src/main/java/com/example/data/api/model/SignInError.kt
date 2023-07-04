package com.example.data.api.model


import com.google.gson.annotations.SerializedName

data class SignInError(
    @SerializedName("error")
    val error: String,
    @SerializedName("error_description")
    val errorDescription: String
)