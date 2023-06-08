package com.example.basicapplication.model.base_model

interface CreateImageInterface: BaseCreateEntity {
    val file: String
    val name: String
}