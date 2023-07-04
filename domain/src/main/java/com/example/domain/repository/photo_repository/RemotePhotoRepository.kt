package com.example.domain.repository.photo_repository

import com.example.domain.entity.PhotoEntity
import io.reactivex.Single
import java.io.File


interface RemotePhotoRepository: PhotoRepository {

    fun create(name: String, description: String, popular: Boolean, new: Boolean, imageFile: File): Single<PhotoEntity>

}