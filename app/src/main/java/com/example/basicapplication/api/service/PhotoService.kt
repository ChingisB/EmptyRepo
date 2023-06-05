package com.example.basicapplication.api.service

import com.example.basicapplication.model.retrofitModel.CreatePhoto
import com.example.basicapplication.model.retrofitModel.Photo
import com.example.basicapplication.model.retrofitModel.PhotoResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface PhotoService {
    @GET("api/photos")
    fun getPhotos(
        @Query("new") new: Boolean,
        @Query("popular") popular: Boolean,
        @Query("page") page: Int
    ): Single<PhotoResponse>


    @GET("api/photos")
    fun getNewPhotos(@Query("page") page: Int) = getPhotos(new = true, popular = false, page = page)

    @GET("api/photos")
    fun getPopularPhotos(@Query("page") page: Int) =
        getPhotos(new = false, popular = true, page = page)

    @GET("api/photos/{id}")
    fun getPhoto(@Path("id") id: Int): Single<Photo>

    @POST("api/photos")
    fun createPhoto(@Body createPhoto: CreatePhoto): Single<Photo>

    @DELETE("api/photos/{id}")
    fun deletePhoto(@Path("id") id: Int): Completable

}