package com.example.basicapplication.model.base_model

interface UpdatePasswordInterface: BaseUpdateEntity{
    val oldPassword: String
    val newPassword: String
}