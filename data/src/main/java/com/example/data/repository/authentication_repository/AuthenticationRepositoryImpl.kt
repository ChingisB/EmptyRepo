package com.example.data.repository.authentication_repository

import com.example.domain.repository.token_repository.TokenRepository
import com.example.data.api.service.AuthenticationService
import com.example.data.api.model.AuthResponse
import com.example.data.api.model.CreateUser
import com.example.data.api.model.User
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val authenticationService: AuthenticationService
) : AuthenticationRepository {

    override fun signIn(username: String, password: String): Single<AuthResponse> =
        authenticationService.login(username = username, password = password)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    override fun saveTokens(auth: AuthResponse){
        saveAccessToken(auth.accessToken)
        saveRefreshToken(auth.refreshToken)
    }

    override fun saveAccessToken(token: String) = tokenRepository.saveAccessToken(token)

    override fun saveRefreshToken(token: String) = tokenRepository.saveRefreshToken(token)

    override fun signUp(item: CreateUser): Single<User> =
        authenticationService.signUp(item).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

}