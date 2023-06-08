package com.example.basicapplication.model.base_model

import com.example.basicapplication.model.retrofit_model.Image

interface PhotoInterface: BaseEntity {

    val id: Int
    val name: String
    val description: String
    val dateCreate: String
    val new: Boolean
    val popular: Boolean
    val image: Image?

    fun getUser()
}