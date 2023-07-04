package com.example.data.repository.authentication_repository

import com.example.data.api.model.AuthResponse
import com.example.data.api.model.CreateUser
import com.example.data.api.model.User
import io.reactivex.Single

interface AuthenticationRepository{

    fun signIn(username: String, password: String): Single<AuthResponse>

    fun signUp(item: CreateUser): Single<User>

    fun saveTokens(auth: AuthResponse)

    fun saveAccessToken(token: String)

    fun saveRefreshToken(token: String)

}