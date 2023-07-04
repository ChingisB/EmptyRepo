package com.example.data.api.model


import com.google.gson.annotations.SerializedName

data class Violation(
    @SerializedName("message")
    val message: String,
    @SerializedName("propertyPath")
    val propertyPath: String
)