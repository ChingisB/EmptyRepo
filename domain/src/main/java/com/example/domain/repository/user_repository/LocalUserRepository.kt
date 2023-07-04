package com.example.domain.repository.user_repository

import com.example.domain.entity.UserEntity
import io.reactivex.Completable

interface LocalUserRepository: UserRepository{

    fun insertUser(userEntity: UserEntity): Completable

    fun deleteLocalUser(): Completable
}