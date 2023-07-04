package com.example.domain.use_case

import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.repository.photo_repository.LocalPhotoRepository
import com.example.domain.repository.photo_repository.RemotePhotoRepository
import io.reactivex.Completable
import io.reactivex.Single

class GetUserPhotosUseCase(
    private val localPhotoRepository: LocalPhotoRepository,
    private val remotePhotoRepository: RemotePhotoRepository
) {
    operator fun invoke(userId: Int, page: Int): Single<PaginatedPhotosEntity> {
        return remotePhotoRepository.getUserPhotos(userId, page).flatMap { photoResponse ->
            Completable.concat(photoResponse.data.map(localPhotoRepository::insertPhoto))
                .andThen(localPhotoRepository.getUserPhotos(userId, page))
        }.onErrorResumeNext { localPhotoRepository.getUserPhotos(userId, page) }
    }
}