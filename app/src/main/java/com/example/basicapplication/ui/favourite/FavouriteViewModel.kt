package com.example.basicapplication.ui.favourite

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.base.PagingViewModel
import com.example.basicapplication.util.Constants
import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.repository.photo_repository.LocalPhotoRepository
import io.reactivex.Single
import javax.inject.Inject

class FavouriteViewModel(private val photoRepository: LocalPhotoRepository): PagingViewModel<PaginatedPhotosEntity>() {

    init{
        refreshData()
    }

    override fun getDataSource(): Single<PaginatedPhotosEntity> = photoRepository.getSavedPhotos(currentPage)

    override fun checkIsLastPage(data: PaginatedPhotosEntity) = data.countOfPages < currentPage

    class Factory @Inject constructor(private val photoRepository: LocalPhotoRepository) :
        AbstractSavedStateViewModelFactory() {

        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return FavouriteViewModel(photoRepository) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }

}