package com.example.basicapplication.data.repository.authentication_repository

import com.example.basicapplication.data.data_source.api.TokenManager
import com.example.basicapplication.data.data_source.api.service.AuthenticationService
import com.example.basicapplication.model.retrofit_model.AuthResponse
import com.example.basicapplication.model.retrofit_model.CreateUser
import com.example.basicapplication.model.retrofit_model.User
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val tokenManager: TokenManager,
    private val authenticationService: AuthenticationService
) :
    AuthenticationRepository<User, CreateUser, AuthResponse> {


    override fun login(username: String, password: String): Single<AuthResponse> =
        authenticationService.login(username = username, password = password)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    override fun saveAccessToken(token: String): Completable =
        Completable.fromAction { tokenManager.saveAccessToken(token) }


    override fun saveRefreshToken(token: String): Completable =
        Completable.fromAction { tokenManager.saveRefreshToken(token)}

    override fun getByID(id: Int): Single<User> {
        TODO("Not yet implemented")
    }

    override fun create(item: CreateUser): Single<User> =
        authenticationService.signUp(item).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    override fun delete(id: Int): Completable =
        authenticationService.deleteUser(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


}