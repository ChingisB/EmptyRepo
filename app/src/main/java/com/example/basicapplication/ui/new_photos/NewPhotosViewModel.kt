package com.example.basicapplication.ui.new_photos


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.data.repository.photo_repository.PhotoRepository
import com.example.basicapplication.model.retrofit_model.CreatePhoto
import com.example.basicapplication.model.retrofit_model.Photo
import com.example.basicapplication.util.BaseViewModel
import com.example.basicapplication.util.Constants
import com.example.basicapplication.util.Resource
import javax.inject.Inject

class NewPhotosViewModel(private val photoRepository: PhotoRepository<Photo, CreatePhoto>) :
    BaseViewModel() {


    private val _photoLiveData = MutableLiveData<Resource<List<Photo>>>()
    val photoLiveData: LiveData<Resource<List<Photo>>> = _photoLiveData
    private var _isLastPage = MutableLiveData(false)
    val isLastPage: LiveData<Boolean>
        get() = _isLastPage

    init {
        fetch(1)
    }

    fun fetch(page: Int) {
        photoRepository.getNewPhotos(page)
            .doOnSubscribe { _photoLiveData.postValue(Resource.Loading()) }
            .subscribe(
                { value ->
                    if (value.isEmpty()) _isLastPage.postValue(true)
                    _photoLiveData.postValue(Resource.Success(value))
                },
                { error ->
                    _photoLiveData.postValue(Resource.Error(message = error.message ?: Constants.networkError))
                    error.printStackTrace()
                }).let(compositeDisposable::add)
    }

    class Factory @Inject constructor(private val photoRepository: PhotoRepository<Photo, CreatePhoto>) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching {
            @Suppress("UNCHECKED_CAST")
            return NewPhotosViewModel(photoRepository) as T
        }.getOrElse { error(Constants.unknownViewModelClassError) }
    }
}