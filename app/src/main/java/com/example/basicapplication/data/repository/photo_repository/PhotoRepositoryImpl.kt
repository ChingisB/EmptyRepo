package com.example.basicapplication.data.repository.photo_repository

import com.example.basicapplication.data.data_source.api.service.PhotoService
import com.example.basicapplication.model.retrofit_model.CreatePhoto
import com.example.basicapplication.model.retrofit_model.Photo
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val photoService: PhotoService) :
    PhotoRepository<Photo, CreatePhoto> {

    override fun getNewPhotos(page: Int, query: String): Single<List<Photo>> {
        val response =
            setUpThreads(photoService.getNewPhotos(page)).delay(700, TimeUnit.MILLISECONDS)
        return response.map { photoResponse -> photoResponse.data }
    }

    override fun getPopularPhotos(page: Int, query: String): Single<List<Photo>> {
        val response =
            setUpThreads(photoService.getPopularPhotos(page)).delay(700, TimeUnit.MILLISECONDS)
        return response.map { photoResponse -> photoResponse.data }
    }

    override fun getByID(id: Int): Single<Photo> = setUpThreads(photoService.getPhoto(id))

    override fun create(item: CreatePhoto): Single<Photo> =
        setUpThreads(photoService.createPhoto(item))

    override fun delete(id: Int): Completable =
        photoService.deletePhoto(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getUserPhotos(userId: Int): Single<List<Photo>> {
        val response = setUpThreads(photoService.getPhotos(userId = userId))
        return response.map { photoResponse -> photoResponse.data }
    }

    private fun <T> setUpThreads(single: Single<T>): Single<T> {
        return single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}