package com.example.basicapplication.ui.photos


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.data.repository.photo_repository.PhotoRepository
import com.example.basicapplication.data.model.Photo
import com.example.basicapplication.util.Constants
import com.example.basicapplication.base.PagingViewModel
import io.reactivex.Single
import javax.inject.Inject

class PhotosViewModel(private val photoRepository: PhotoRepository) :
    PagingViewModel<Photo>() {

    var new = true
    var popular = false

    init{
        loadPage()
    }

    class Factory @Inject constructor(private val photoRepository: PhotoRepository) :
        ViewModelProvider.Factory  {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return PhotosViewModel(photoRepository) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }

    override fun getDataSource(): Single<List<Photo>> {
        return photoRepository.getPhotos(page, query, new, popular)
    }
}