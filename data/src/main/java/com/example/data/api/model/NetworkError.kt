package com.example.data.api.model


import com.google.gson.annotations.SerializedName

data class NetworkError(
    @SerializedName("detail")
    val detail: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("violations")
    val violations: List<Violation>
)