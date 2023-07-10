package com.example.basicapplication

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.util.Constants
import com.example.data.FileFromUriResolver
import java.io.File
import javax.inject.Inject

class SharedImageViewModel(private val fileResolver: FileFromUriResolver): ViewModel() {

    private val _imageLiveData = MutableLiveData<File?>(null)
    val imageLiveData: LiveData<File?> = _imageLiveData

    
    fun setImageFile(imageUri: Uri) = _imageLiveData.postValue(fileResolver.parseFileFromUri(imageUri))

    fun clearImageFile() = _imageLiveData.postValue(null)


    class Factory @Inject constructor(private val fileResolver: FileFromUriResolver): ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching {
            return SharedImageViewModel(fileResolver) as T
        }.getOrElse { error(Constants.UNKNOWN_VIEW_MODEL_CLASS_ERROR) }
    }
}