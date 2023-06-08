package com.example.basicapplication.model.base_model

interface UserInterface: BaseEntity {
    val id: Int
    val email: String
    val enabled: Boolean
    val birthday: String
    val roles: List<String>
    val username: String
}