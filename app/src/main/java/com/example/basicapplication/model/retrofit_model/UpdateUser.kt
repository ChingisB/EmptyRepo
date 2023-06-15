package com.example.basicapplication.model.retrofit_model

import com.example.basicapplication.model.base_model.UpdateUserInterface
import com.google.gson.annotations.SerializedName

data class UpdateUser(
    @SerializedName("email")
    override val email: String,
    @SerializedName("username")
    override val username: String,
    @SerializedName("birthday")
    override val birthday: String
): UpdateUserInterface