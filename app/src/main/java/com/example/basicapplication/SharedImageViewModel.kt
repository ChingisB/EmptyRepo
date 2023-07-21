package com.example.basicapplication

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.FileFromUriResolver
import java.io.File
import javax.inject.Inject

class SharedImageViewModel @Inject constructor(private val fileResolver: FileFromUriResolver): ViewModel() {

    private val _imageLiveData = MutableLiveData<File?>(null)
    val imageLiveData: LiveData<File?> = _imageLiveData

    
    fun setImageFile(imageUri: Uri) = _imageLiveData.postValue(fileResolver.parseFileFromUri(imageUri))

    fun clearImageFile() = _imageLiveData.postValue(null)
}