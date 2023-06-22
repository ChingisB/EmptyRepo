package com.example.basicapplication.data.model


import com.google.gson.annotations.SerializedName

data class  CreateUser(
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("confirm_password")
    val confirmPassword: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)