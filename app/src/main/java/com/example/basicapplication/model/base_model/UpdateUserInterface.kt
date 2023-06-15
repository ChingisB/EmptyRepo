package com.example.basicapplication.model.base_model

interface UpdateUserInterface: BaseUpdateEntity{
    val email: String
    val username: String
    val birthday: String
}