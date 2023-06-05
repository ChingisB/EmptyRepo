package com.example.basicapplication.repository.authenticationRepository

import com.example.basicapplication.model.baseModel.BaseAuthEntity
import com.example.basicapplication.model.baseModel.CreateUserInterface
import com.example.basicapplication.model.baseModel.UserInterface
import com.example.basicapplication.util.Repository
import io.reactivex.Completable
import io.reactivex.Single

interface AuthenticationRepository<T : UserInterface, R : CreateUserInterface, K: BaseAuthEntity> :
    Repository<T, Int, R> {

    fun login(username: String, password: String): Single<K>


    fun saveAccessToken(token: String): Completable

    fun saveRefreshToken(token: String): Completable

}