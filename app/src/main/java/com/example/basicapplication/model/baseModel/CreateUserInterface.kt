package com.example.basicapplication.model.baseModel

interface CreateUserInterface: BaseCreateEntity {
    val email: String
    val password: String
    val username: String
}