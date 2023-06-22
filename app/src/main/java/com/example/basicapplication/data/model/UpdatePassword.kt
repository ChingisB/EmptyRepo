package com.example.basicapplication.data.model

import com.google.gson.annotations.SerializedName

data class UpdatePassword(
    @SerializedName("oldPassword")
    val oldPassword: String,
    @SerializedName("newPassword")
    val newPassword: String)