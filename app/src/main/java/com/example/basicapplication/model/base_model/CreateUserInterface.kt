package com.example.basicapplication.model.base_model

interface CreateUserInterface: BaseCreateEntity {
    val email: String
    val password: String
    val username: String
}