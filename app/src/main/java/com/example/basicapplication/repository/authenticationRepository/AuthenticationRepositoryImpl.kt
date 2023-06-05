package com.example.basicapplication.repository.authenticationRepository

import android.content.SharedPreferences
import com.example.basicapplication.api.service.AuthenticationService
import com.example.basicapplication.model.retrofitModel.AuthResponse
import com.example.basicapplication.model.retrofitModel.CreateUser
import com.example.basicapplication.model.retrofitModel.User
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val preferences: SharedPreferences,
    private val authenticationService: AuthenticationService
) :
    AuthenticationRepository<User, CreateUser, AuthResponse> {


    override fun login(username: String, password: String): Single<AuthResponse> =
        authenticationService.login(username = username, password = password)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    override fun saveAccessToken(token: String): Completable =
        Completable.fromAction { preferences.edit().putString("AccessToken", token).apply() }


    override fun saveRefreshToken(token: String): Completable =
        Completable.fromAction { preferences.edit().putString("RefreshToken", token).apply() }

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