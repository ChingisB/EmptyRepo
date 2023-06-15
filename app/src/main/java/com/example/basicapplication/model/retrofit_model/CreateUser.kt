package com.example.basicapplication.model.retrofit_model


import com.example.basicapplication.model.base_model.CreateUserInterface
import com.google.gson.annotations.SerializedName

data class  CreateUser(
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("confirm_password")
    val confirmPassword: String,
    @SerializedName("email")
    override val email: String,
    @SerializedName("password")
    override val password: String,
    @SerializedName("username")
    override val username: String
): CreateUserInterface