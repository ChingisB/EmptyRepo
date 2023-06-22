package com.example.basicapplication.data.repository.authentication_repository

import com.example.basicapplication.data.repository.token_repository.TokenRepository
import com.example.basicapplication.data.data_source.api.service.AuthenticationService
import com.example.basicapplication.data.model.AuthResponse
import com.example.basicapplication.data.model.CreateUser
import com.example.basicapplication.data.model.User
import io.reactivex.Completable
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

    override fun saveTokens(auth: AuthResponse): Completable =
        saveAccessToken(auth.accessToken).andThen(saveRefreshToken(auth.refreshToken))


    override fun saveAccessToken(token: String): Completable = Completable.fromAction { tokenRepository.saveAccessToken(token) }

    override fun saveRefreshToken(token: String): Completable = Completable.fromAction { tokenRepository.saveRefreshToken(token) }

    override fun signUp(item: CreateUser): Single<User> =
        authenticationService.signUp(item).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

}