package com.example.basicapplication.data.repository.photo_repository


import android.net.Uri
import com.example.basicapplication.data.model.Photo
import io.reactivex.Completable
import io.reactivex.Single


interface PhotoRepository {

    fun getPhotos(page: Int, query: String?, new: Boolean, popular: Boolean): Single<List<Photo>>

    fun getByID(id: Int): Single<Photo>

    fun create(name: String, description: String, imageUri: Uri, new: Boolean, popular: Boolean):  Single<Photo>?

    fun delete(id: Int): Completable

    fun getUserPhotos(userId: Int, page: Int): Single<List<Photo>>
}