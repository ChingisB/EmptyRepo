package com.example.data.api.model

import com.google.gson.annotations.SerializedName

data class UpdatePassword(
    @SerializedName("oldPassword")
    val oldPassword: String,
    @SerializedName("newPassword")
    val newPassword: String)