package com.example.basicapplication.ui.new_photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.data.repository.photo_repository.PhotoRepository
import com.example.basicapplication.model.retrofit_model.CreatePhoto
import com.example.basicapplication.model.retrofit_model.Photo
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class NewPhotosViewModel(private val photoRepository: PhotoRepository<Photo, CreatePhoto>) :
    ViewModel() {


    private val compositeDisposable = CompositeDisposable()
    private val _photoLiveData = MutableLiveData<List<Photo>>()
    val photoLiveData: LiveData<List<Photo>> = _photoLiveData
    private var _isLastPage = MutableLiveData(false)
    val isLastPage: LiveData<Boolean>
        get() = _isLastPage


    fun fetch(page: Int) {
        val disposable = photoRepository.getNewPhotos(page).subscribe(
            { value ->
                if (value.isEmpty()) {
                    _isLastPage.postValue(true)
                } else {
                    _photoLiveData.postValue(
                        _photoLiveData.value?.plus(value) ?: value
                    )
                }
            },
            { error -> error.printStackTrace() }
        )
        compositeDisposable.add(disposable)
    }


    class Factory @Inject constructor(private val photoRepository: PhotoRepository<Photo, CreatePhoto>) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewPhotosViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewPhotosViewModel(photoRepository) as T
            }
            throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
        }
    }
}