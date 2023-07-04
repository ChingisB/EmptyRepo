package com.example.domain.repository.user_repository

import com.example.domain.entity.UserEntity
import io.reactivex.Single

interface RemoteUserRepository: UserRepository{

    fun updatePassword(id: Int, oldPassword: String, newPassword: String): Single<UserEntity>

    fun updateUser(id: Int, email: String, birthday: String, username: String): Single<UserEntity>
}