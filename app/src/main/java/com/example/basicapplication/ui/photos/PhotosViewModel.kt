package com.example.basicapplication.ui.photos


import com.example.base.PagingViewModel
import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.use_case.GetPhotosUseCase
import javax.inject.Inject

class PhotosViewModel @Inject constructor(private val getPhotosUseCase: GetPhotosUseCase) : PagingViewModel<PaginatedPhotosEntity>() {

    var new = false
    var popular = false
    private var query: String = ""

    override fun getDataSource() = getPhotosUseCase.invoke(page = currentPage, query = query, new = new, popular = popular)

    override fun checkIsLastPage(data: PaginatedPhotosEntity) = data.countOfPages < currentPage

    fun refreshDataWithNewQuery(query: String) {
        this.query = query
        refreshData()
    }

}