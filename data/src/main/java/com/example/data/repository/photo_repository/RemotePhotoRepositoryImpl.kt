package com.example.data.repository.photo_repository

import com.example.data.api.model.CreatePhoto
import com.example.data.api.model.PhotoResponse
import com.example.data.api.service.ImageService
import com.example.data.api.service.PhotoService
import com.example.data.mapper.image.ImageApiToEntityMapper
import com.example.data.mapper.photo.PhotoApiToEntityMapper
import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.entity.PhotoEntity
import com.example.domain.repository.photo_repository.RemotePhotoRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class RemotePhotoRepositoryImpl @Inject constructor(
    private val photoService: PhotoService,
    private val imageService: ImageService
) : RemotePhotoRepository {

    private val photoApiToEntityMapper = PhotoApiToEntityMapper(ImageApiToEntityMapper())

    override fun create(name: String, description: String, popular: Boolean, new: Boolean, imageFile: File): Single<PhotoEntity> =
        imageService.createImage(MultipartBody.Part.createFormData(
            "file",
            imageFile.name,
            RequestBody.create(MediaType.parse("image/*"), imageFile))
        ).flatMap {photoService.createPhoto(CreatePhoto(
            name = name,
            description = description,
            new = new,
            popular = popular,
            image = "api/media_objects/${it.id}",
            )
        ).map(photoApiToEntityMapper::convert)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    override fun getPhotos(new: Boolean, popular: Boolean, query: String, page: Int): Single<PaginatedPhotosEntity> =
        photoService.getPhotos(new = new, popular = popular, query = query, page = page)
            .map(this::convertPhotoResponseToEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    override fun getUserPhotos(userId: Int, page: Int): Single<PaginatedPhotosEntity> =
        photoService.getPhotos(userId = userId, page = page)
            .map(this::convertPhotoResponseToEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getById(id: Int): Single<PhotoEntity> =
        photoService.getPhoto(id)
            .map(photoApiToEntityMapper::convert)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    override fun delete(id: Int) = photoService.deletePhoto(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())

    private fun convertPhotoResponseToEntity(photoResponse: PhotoResponse) =
        PaginatedPhotosEntity(
            countOfPages = photoResponse.countOfPages,
            itemsPerPage = photoResponse.itemsPerPage.toLong(),
            totalItems = photoResponse.totalItems,
            data = photoResponse.data.map(photoApiToEntityMapper::convert).toMutableList()
        )

}