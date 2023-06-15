package com.example.basicapplication.data.repository.photo_repository

import com.example.basicapplication.model.base_model.CreatePhotoInterface
import com.example.basicapplication.model.base_model.PhotoInterface
import io.reactivex.Completable
import io.reactivex.Single


interface PhotoRepository<T: PhotoInterface, R: CreatePhotoInterface> {


    fun getNewPhotos(page: Int, query: String = ""): Single<List<T>>

    fun getPopularPhotos(page: Int, query: String = ""): Single<List<T>>

    fun getByID(id: Int): Single<T>

    fun create(item: R):  Single<T>

    fun delete(id: Int): Completable

    fun getUserPhotos(userId: Int): Single<List<T>>
}