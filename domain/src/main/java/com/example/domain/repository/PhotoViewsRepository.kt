package com.example.domain.repository

import com.example.domain.entity.PhotoEntity

interface PhotoViewsRepository {

    fun addPhotoView(photoEntity: PhotoEntity)

    fun getUserTotalViews(userId: Int, callback: (Long) -> Unit)

    fun getPhotoViews(photoId: Int, callback: (Long) -> Unit)
}