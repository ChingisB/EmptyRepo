package com.example.basicapplication.model.base_model

interface CreatePhotoInterface: BaseCreateEntity {
    val name: String
    val dateCreate: String
    val new: Boolean
    val popular: Boolean
    val image: ImageInterface
    val description: String
}