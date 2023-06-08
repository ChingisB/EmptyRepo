package com.example.basicapplication.data.repository.photo_repository

import com.example.basicapplication.data.data_source.api.service.PhotoService
import com.example.basicapplication.model.retrofit_model.CreatePhoto
import com.example.basicapplication.model.retrofit_model.Photo
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val photoService: PhotoService) :
    PhotoRepository<Photo, CreatePhoto> {

    override fun getNewPhotos(page: Int, query: String): Single<List<Photo>> {
        val response =
            photoService.getNewPhotos(page).subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            )
        return response.map { photoResponse -> photoResponse.data }

    }

    override fun getPopularPhotos(page: Int, query: String): Single<List<Photo>> {
        val response =
            photoService.getPopularPhotos(page).subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            )
        return response.map { photoResponse -> photoResponse.data }
    }

    override fun getByID(id: Int): Single<Photo> =
        photoService.getPhoto(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    override fun create(item: CreatePhoto): Single<Photo> =
        photoService.createPhoto(item).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    override fun delete(id: Int): Completable =
        photoService.deletePhoto(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


}