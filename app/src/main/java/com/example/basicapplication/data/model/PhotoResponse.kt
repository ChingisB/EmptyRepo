package com.example.basicapplication.data.model


import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("countOfPages")
    val countOfPages: Int,
    @SerializedName("data")
    val data: List<Photo>,
    @SerializedName("itemsPerPage")
    val itemsPerPage: Int,
    @SerializedName("totalItems")
    val totalItems: Int
)