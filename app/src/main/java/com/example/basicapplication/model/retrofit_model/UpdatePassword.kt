package com.example.basicapplication.model.retrofit_model

import com.example.basicapplication.model.base_model.UpdatePasswordInterface
import com.google.gson.annotations.SerializedName

data class UpdatePassword(
    @SerializedName("oldPassword")
    override val oldPassword: String,
    @SerializedName("newPassword")
    override val newPassword: String): UpdatePasswordInterface