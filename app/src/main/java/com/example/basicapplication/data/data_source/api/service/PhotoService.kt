package com.example.basicapplication.data.data_source.api.service

import com.example.basicapplication.data.model.CreatePhoto
import com.example.basicapplication.data.model.Photo
import com.example.basicapplication.data.model.PhotoResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface PhotoService {
    @GET("api/photos")
    fun getPhotos(
        @Query("new") new: Boolean? = null,
        @Query("popular") popular: Boolean? = null,
        @Query("page") page: Int? = null,
        @Query("user.id") userId: Int? = null,
        @Query("name") query: String? = null
    ): Single<PhotoResponse>

    @GET("api/photos/{id}")
    fun getPhoto(@Path("id") id: Int): Single<Photo>

    @POST("api/photos")
    fun createPhoto(@Body createPhoto: CreatePhoto): Single<Photo>

    @DELETE("api/photos/{id}")
    fun deletePhoto(@Path("id") id: Int): Completable

}