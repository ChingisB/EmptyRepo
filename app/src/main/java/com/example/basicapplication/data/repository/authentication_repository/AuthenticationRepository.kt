package com.example.basicapplication.data.repository.authentication_repository

import com.example.basicapplication.model.base_model.BaseAuthEntity
import com.example.basicapplication.model.base_model.CreateUserInterface
import com.example.basicapplication.model.base_model.UserInterface
import io.reactivex.Completable
import io.reactivex.Single

interface AuthenticationRepository<T : UserInterface, R : CreateUserInterface, K: BaseAuthEntity>{

    fun signIn(username: String, password: String): Single<K>

    fun signUp(item: R): Single<T>

    fun saveTokens(auth: K): Completable

    fun saveAccessToken(token: String): Completable

    fun saveRefreshToken(token: String): Completable

}