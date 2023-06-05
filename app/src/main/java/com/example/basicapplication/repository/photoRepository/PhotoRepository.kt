package com.example.basicapplication.repository.photoRepository

import com.example.basicapplication.model.baseModel.CreatePhotoInterface
import com.example.basicapplication.model.baseModel.PhotoInterface
import com.example.basicapplication.util.Repository
import io.reactivex.Single


interface PhotoRepository<T: PhotoInterface, R: CreatePhotoInterface> :
    Repository<T, Int, R> {


    fun getNewPhotos(page: Int, query: String = ""): Single<List<T>>

    fun getPopularPhotos(page: Int, query: String = ""): Single<List<T>>
}