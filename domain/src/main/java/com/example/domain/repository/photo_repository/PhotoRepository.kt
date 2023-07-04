package com.example.domain.repository.photo_repository

import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.entity.PhotoEntity
import io.reactivex.Completable
import io.reactivex.Single

interface PhotoRepository {

    fun getPhotos(new: Boolean, popular: Boolean, query: String = "", page: Int): Single<PaginatedPhotosEntity>

    fun getUserPhotos(userId: Int, page: Int): Single<PaginatedPhotosEntity>

    fun getById(id: Int): Single<PhotoEntity>

    fun delete(id: Int): Completable
}