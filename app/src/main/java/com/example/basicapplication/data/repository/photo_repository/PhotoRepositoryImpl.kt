package com.example.basicapplication.data.repository.photo_repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.basicapplication.data.data_source.api.service.ImageService
import com.example.basicapplication.data.data_source.api.service.PhotoService
import com.example.basicapplication.data.model.CreatePhoto
import com.example.basicapplication.data.model.Photo
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoService: PhotoService,
    private val imageService: ImageService,
    private val context: Context
) : PhotoRepository {

    override fun getPhotos(page: Int, query: String?, new: Boolean, popular: Boolean): Single<List<Photo>>{
        val response =
            setUpThreads(photoService.getPhotos(new = true, popular = popular, page = page, query = query))
                .delay(700, TimeUnit.MILLISECONDS)
        return response.map { photoResponse -> photoResponse.data }
    }

    override fun getByID(id: Int): Single<Photo> = setUpThreads(photoService.getPhoto(id))

    override fun create(name: String, description: String, imageUri: Uri, new: Boolean, popular: Boolean): Single<Photo>? {
        val imageFile = parseFileFromUri(imageUri)
        val imageType = context.contentResolver.getType(imageUri)

        if (imageType != null && imageFile != null) {
            val imageBody = RequestBody.create(MediaType.parse(imageType), imageFile)
            val imagePart = MultipartBody.Part.createFormData("file", imageFile.name, imageBody)
            return setUpThreads(imageService.createImage(imagePart)).flatMap { image ->
                val createPhoto = CreatePhoto(
                    name = name,
                    description = description,
                    new = new,
                    popular = popular,
                    dateCreate = LocalDateTime.now().toString(),
                    image = "api/media_objects/${image.id}"
                )
                setUpThreads(photoService.createPhoto(createPhoto))
            }
        }
        return null
    }

    private fun parseFileFromUri(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        filePath?.let { return File(it) }
        return null
    }

    override fun delete(id: Int): Completable =
        photoService.deletePhoto(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getUserPhotos(userId: Int, page: Int): Single<List<Photo>> {
        val response = setUpThreads(photoService.getPhotos(userId = userId, page = page))
        return response.map { photoResponse -> photoResponse.data }
    }

    private fun <T> setUpThreads(single: Single<T>): Single<T> {
        return single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}