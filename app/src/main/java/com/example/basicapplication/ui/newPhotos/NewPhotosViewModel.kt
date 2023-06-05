package com.example.basicapplication.ui.newPhotos


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.model.retrofitModel.CreatePhoto
import com.example.basicapplication.model.retrofitModel.Photo
import com.example.basicapplication.repository.photoRepository.PhotoRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class NewPhotosViewModel(private val photoRepository: PhotoRepository<Photo, CreatePhoto>) :
    ViewModel() {


    private val compositeDisposable = CompositeDisposable()
    private val _photoLiveData = MutableLiveData<List<Photo>>()
    val photoLiveData: LiveData<List<Photo>> = _photoLiveData


    fun fetch() {
        val disposable = photoRepository.getNewPhotos(1).subscribe(
            { value ->
                _photoLiveData.postValue(
                    value
                )
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