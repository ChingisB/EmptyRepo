package com.example.basicapplication.data.repository.photo_repository

import com.example.basicapplication.model.base_model.CreatePhotoInterface
import com.example.basicapplication.model.base_model.PhotoInterface
import com.example.basicapplication.util.Repository
import io.reactivex.Single


interface PhotoRepository<T: PhotoInterface, R: CreatePhotoInterface> :
    Repository<T, Int, R> {


    fun getNewPhotos(page: Int, query: String = ""): Single<List<T>>

    fun getPopularPhotos(page: Int, query: String = ""): Single<List<T>>
}