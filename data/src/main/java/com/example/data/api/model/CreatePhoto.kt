package com.example.data.api.model


import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class CreatePhoto(
    @SerializedName("dateCreate")
    val dateCreate: String = LocalDate.now().toString(),
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