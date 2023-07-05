package com.example.basicapplication.ui.photos


import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.base.PagingViewModel
import com.example.basicapplication.util.Constants
import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.use_case.GetPhotosUseCase
import io.reactivex.Single
import javax.inject.Inject

class PhotosViewModel(private val getPhotosUseCase: GetPhotosUseCase) : PagingViewModel<PaginatedPhotosEntity>() {

    var new = false
    var popular = false
    private var query: String = ""

    override fun getDataSource(): Single<PaginatedPhotosEntity> =
        getPhotosUseCase.invoke(page = currentPage, query = query, new = new, popular = popular)

    override fun checkIsLastPage(data: PaginatedPhotosEntity): Boolean = data.countOfPages < currentPage

    fun refreshDataWithNewQuery(query: String) {
        this.query = query
        refreshData()
    }

    class Factory @Inject constructor(private val getPhotosUseCase: GetPhotosUseCase) :
        AbstractSavedStateViewModelFactory() {

        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return PhotosViewModel(getPhotosUseCase) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }

}