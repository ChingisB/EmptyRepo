package com.example.basicapplication.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.basicapplication.data.model.Photo
import com.example.basicapplication.util.Constants
import com.example.basicapplication.util.Resource
import io.reactivex.Single

abstract class PagingViewModel<Data>: BaseViewModel(){

    private val _data = MutableLiveData<Resource<List<Data>>>()
    val data: LiveData<Resource<List<Data>>> = _data
    private val _isLastPage = MutableLiveData(false)
    val isLastPage: LiveData<Boolean> = _isLastPage
    var query: String? = ""
    var page = 1


    fun loadPage(){
        getDataSource().doOnSubscribe { _data.postValue(Resource.Loading()) }
            .subscribe(
                { value ->
                    page++
                    if (value.isEmpty()) _isLastPage.postValue(true)
                    _data.postValue(Resource.Success(value))
                },
                { error ->
                    _data.postValue(Resource.Error(message = error.message ?: Constants.NETWORK_ERROR))
                    error.printStackTrace()
                }).let(compositeDisposable::add)
    }

    abstract fun getDataSource(): Single<List<Data>>
}