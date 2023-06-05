package com.example.basicapplication.model.baseModel

interface PhotoInterface: BaseEntity {

    val id: Int
    val name: String
    val description: String
    val dateCreate: String
    val new: Boolean
    val popular: Boolean
    val image: ImageInterface

    fun getUser()
}