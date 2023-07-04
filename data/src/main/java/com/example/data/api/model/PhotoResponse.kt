package com.example.data.api.model

import com.example.domain.MutableData
import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("countOfPages")
    val countOfPages: Int,
    @SerializedName("data")
    val data: MutableList<Photo>,
    @SerializedName("itemsPerPage")
    val itemsPerPage: Int,
    @SerializedName("totalItems")
    val totalItems: Int
): MutableData {

    override fun add(other: MutableData) {
        if(other is PhotoResponse){
            data.addAll(other.data)
        }
    }
}