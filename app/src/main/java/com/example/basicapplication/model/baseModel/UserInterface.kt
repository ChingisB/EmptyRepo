package com.example.basicapplication.model.baseModel

interface UserInterface: BaseEntity {
    val id: Int
    val email: String
    val enabled: Boolean
    val birthday: String
    val roles: List<String>
    val username: String
}