package com.example.basicapplication.data.repository.authentication_repository

import com.example.basicapplication.model.base_model.BaseAuthEntity
import com.example.basicapplication.model.base_model.CreateUserInterface
import com.example.basicapplication.model.base_model.UserInterface
import com.example.basicapplication.util.Repository
import io.reactivex.Completable
import io.reactivex.Single

interface AuthenticationRepository<T : UserInterface, R : CreateUserInterface, K: BaseAuthEntity> :
    Repository<T, Int, R> {

    fun login(username: String, password: String): Single<K>


    fun saveAccessToken(token: String): Completable

    fun saveRefreshToken(token: String): Completable

}