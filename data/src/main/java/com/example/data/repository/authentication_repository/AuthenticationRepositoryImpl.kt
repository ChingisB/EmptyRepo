package com.example.data.repository.authentication_repository

import com.example.data.api.model.AuthResponse
import com.example.data.api.model.CreateUser
import com.example.data.api.model.User
import com.example.data.api.service.AuthenticationService
import com.example.domain.repository.token_repository.TokenRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val authenticationService: AuthenticationService
) : AuthenticationRepository {

    override fun signIn(username: String, password: String): Single<AuthResponse> =
        authenticationService.login(username = username, password = password).map {
            saveTokens(it)
            it
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    override fun signUp(username: String, birthday: String, email: String, password: String, confirmPassword: String): Single<User> {
        return authenticationService.signUp(
            CreateUser(
                birthday = birthday,
                username = username,
                password = password,
                confirmPassword = confirmPassword,
                email = email
            )
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun saveTokens(auth: AuthResponse) {
        saveAccessToken(auth.accessToken)
        saveRefreshToken(auth.refreshToken)
    }

    override fun saveAccessToken(token: String) = tokenRepository.saveAccessToken(token)

    override fun saveRefreshToken(token: String) = tokenRepository.saveRefreshToken(token)

}