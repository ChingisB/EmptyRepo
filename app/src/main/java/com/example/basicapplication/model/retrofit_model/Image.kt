package com.example.basicapplication.model.retrofit_model


import com.example.basicapplication.model.base_model.ImageInterface
import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id")
    override val id: Int,
    @SerializedName("name")
    override val name: String
) : ImageInterface