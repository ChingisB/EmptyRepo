package com.example.basicapplication.data.repository.authentication_repository

import com.example.basicapplication.data.model.AuthResponse
import com.example.basicapplication.data.model.CreateUser
import com.example.basicapplication.data.model.User
import io.reactivex.Completable
import io.reactivex.Single

interface AuthenticationRepository{

    fun signIn(username: String, password: String): Single<AuthResponse>

    fun signUp(item: CreateUser): Single<User>

    fun saveTokens(auth: AuthResponse): Completable

    fun saveAccessToken(token: String): Completable

    fun saveRefreshToken(token: String): Completable

}