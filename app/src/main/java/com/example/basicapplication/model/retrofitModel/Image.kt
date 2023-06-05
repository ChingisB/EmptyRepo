package com.example.basicapplication.model.retrofitModel


import com.example.basicapplication.model.baseModel.ImageInterface
import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id")
    override val id: Int,
    @SerializedName("name")
    override val name: String
): ImageInterface