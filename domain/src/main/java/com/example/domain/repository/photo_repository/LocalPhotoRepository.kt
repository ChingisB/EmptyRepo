package com.example.domain.repository.photo_repository

import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.entity.PhotoEntity
import io.reactivex.Completable
import io.reactivex.Single

interface LocalPhotoRepository: PhotoRepository {

    fun getSavedPhotos(page: Int): Single<PaginatedPhotosEntity>

    fun savePhoto(photoEntity: PhotoEntity): Completable

    fun removePhoto(photoEntity: PhotoEntity): Completable

    fun insertPhoto(photoEntity: PhotoEntity): Completable

    fun clearLocalPhotos(): Completable


}