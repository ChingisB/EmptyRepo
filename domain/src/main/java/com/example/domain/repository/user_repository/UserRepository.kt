package com.example.domain.repository.user_repository

import com.example.domain.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {

    fun deleteUser(id: Int): Completable

    fun getCurrentUser(): Single<UserEntity>

}