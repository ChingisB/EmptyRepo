package com.example.basicapplication.base

import androidx.viewbinding.ViewBinding
import com.example.basicapplication.data.model.Photo
import com.example.basicapplication.ui.adapter.PhotoListAdapter

abstract class PhotoPagingFragment<VBinding : ViewBinding, ViewModel : PagingViewModel<Photo>> :
    PagingFragment<VBinding, Photo, ViewModel, PhotoListAdapter>() {
}