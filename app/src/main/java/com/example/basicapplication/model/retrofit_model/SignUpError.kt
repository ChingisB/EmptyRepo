package com.example.basicapplication.model.retrofit_model


import com.google.gson.annotations.SerializedName

data class SignUpError(
    @SerializedName("detail")
    val detail: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("violations")
    val violations: List<Violation>
)