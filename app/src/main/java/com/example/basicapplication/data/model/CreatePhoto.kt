package com.example.basicapplication.data.model


import com.google.gson.annotations.SerializedName

data class CreatePhoto(
    @SerializedName("dateCreate")
    val dateCreate: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("new")
    val new: Boolean,
    @SerializedName("popular")
    val popular: Boolean
)