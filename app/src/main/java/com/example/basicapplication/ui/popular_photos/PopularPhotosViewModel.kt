package com.example.basicapplication.ui.popular_photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.data.repository.photo_repository.PhotoRepository
import com.example.basicapplication.model.retrofit_model.CreatePhoto
import com.example.basicapplication.model.retrofit_model.Photo
import com.example.basicapplication.util.BaseViewModel
import com.example.basicapplication.util.Constants
import javax.inject.Inject

class PopularPhotosViewModel(private val photoRepository: PhotoRepository<Photo, CreatePhoto>) :
    BaseViewModel() {

    class Factory @Inject constructor(private val photoRepository: PhotoRepository<Photo, CreatePhoto>) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return PopularPhotosViewModel(photoRepository) as T
        }.getOrElse { error(Constants.unknownViewModelClassError) }
    }
}