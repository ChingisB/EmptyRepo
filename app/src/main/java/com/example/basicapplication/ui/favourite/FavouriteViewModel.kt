package com.example.basicapplication.ui.favourite

import com.example.base.PagingViewModel
import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.repository.photo_repository.LocalPhotoRepository
import io.reactivex.Single
import javax.inject.Inject

class FavouriteViewModel @Inject constructor(private val photoRepository: LocalPhotoRepository): PagingViewModel<PaginatedPhotosEntity>() {

    init{ refreshData() }

    override fun getDataSource(): Single<PaginatedPhotosEntity> = photoRepository.getSavedPhotos(currentPage)

    override fun checkIsLastPage(data: PaginatedPhotosEntity) = data.countOfPages < currentPage

}