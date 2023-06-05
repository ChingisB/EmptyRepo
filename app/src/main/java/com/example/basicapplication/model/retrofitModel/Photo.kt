package com.example.basicapplication.model.retrofitModel


import com.example.basicapplication.model.baseModel.PhotoInterface
import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("dateCreate")
    override val dateCreate: String,
    @SerializedName("description")
    override val description: String,
    @SerializedName("id")
    override val id: Int,
    @SerializedName("image")
    override val image: Image,
    @SerializedName("name")
    override val name: String,
    @SerializedName("new")
    override val new: Boolean,
    @SerializedName("popular")
    override val popular: Boolean,
    @SerializedName("user")
    val user: String
): PhotoInterface {

    override fun getUser() {
        TODO("Not yet implemented")
    }
}