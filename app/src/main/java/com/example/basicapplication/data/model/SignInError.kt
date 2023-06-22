package com.example.basicapplication.data.model


import com.google.gson.annotations.SerializedName

data class SignInError(
    @SerializedName("error")
    val error: String,
    @SerializedName("error_description")
    val errorDescription: String
)