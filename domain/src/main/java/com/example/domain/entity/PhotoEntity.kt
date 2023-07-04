package com.example.domain.entity

data class PhotoEntity(
    val id: Int,
    val name: String,
    val description: String,
    val dateCreate: String,
    val image: ImageEntity,
    val new: Boolean,
    val popular: Boolean,
    val userID: Int,
    var isSaved: Boolean
)