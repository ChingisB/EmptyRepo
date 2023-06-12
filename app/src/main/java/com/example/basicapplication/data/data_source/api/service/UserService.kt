package com.example.basicapplication.data.data_source.api.service

import com.example.basicapplication.model.retrofit_model.User
import io.reactivex.Single
import retrofit2.http.GET

interface UserService {
    @GET("api/users/current")
    fun getCurrentUser(): Single<User>
}