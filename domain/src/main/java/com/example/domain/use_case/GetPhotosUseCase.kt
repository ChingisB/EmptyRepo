package com.example.domain.use_case

import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.repository.photo_repository.LocalPhotoRepository
import com.example.domain.repository.photo_repository.RemotePhotoRepository
import io.reactivex.Completable
import io.reactivex.Single

class GetPhotosUseCase(private val localPhotoRepository: LocalPhotoRepository, private val remotePhotoRepository: RemotePhotoRepository) {
    operator fun invoke(page: Int, new: Boolean, popular: Boolean, query: String = ""): Single<PaginatedPhotosEntity> {
        val localPhotos = localPhotoRepository.getPhotos(new = new, popular = popular, query = query, page = page)
        return remotePhotoRepository.getPhotos(new = new, popular = popular, query = query, page = page).flatMap { photoResponse ->
            Completable.concat(photoResponse.data.map { localPhotoRepository.insertPhoto(it) }).andThen(localPhotos)
        }.onErrorResumeNext { localPhotos }
    }

}