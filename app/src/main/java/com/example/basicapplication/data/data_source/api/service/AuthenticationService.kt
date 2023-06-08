package com.example.basicapplication.data.data_source.api.service

import com.example.basicapplication.data.data_source.api.Config
import com.example.basicapplication.model.retrofit_model.AuthResponse
import com.example.basicapplication.model.retrofit_model.CreateUser
import com.example.basicapplication.model.retrofit_model.User
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*


interface AuthenticationService {

    @POST("api/users")
    fun signUp(@Body createUser: CreateUser): Single<User>


    @GET("oauth/v2/token")
    fun login(
        @Query("client_id") clientId: String = Config.clientId,
        @Query("grant_type") grantType: String = "password",
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("client_secret") clientSecret: String = Config.clientSecret
    ): Single<AuthResponse>


    @GET("oauth/v2/token")
    fun refreshToken(
        @Query("client_id") clientId: String = Config.clientId,
        @Query("grant_type") grantType: String = "refresh_token",
        @Query("refresh_token") refreshToken: String,
        @Query("client_secret") clientSecret: String = Config.clientSecret
    ): Single<AuthResponse>


    @DELETE("api/users/{id}")
    fun deleteUser(@Query("id") id: Int): Completable


}