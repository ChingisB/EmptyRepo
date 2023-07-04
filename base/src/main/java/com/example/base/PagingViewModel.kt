package com.example.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.MutableData
import com.example.util.Resource
import io.reactivex.Single

abstract class PagingViewModel<Data: MutableData>: BaseViewModel(){

    private val _data = MutableLiveData<Resource<Data>>()
    val data: LiveData<Resource<Data>> = _data
    private val _isLastPage = MutableLiveData(false)
    val isLastPage: LiveData<Boolean> = _isLastPage
    private var mutableData: Data? = null
    var currentPage = 1


    open fun loadPage(){
        getDataSource().doOnSubscribe { _data.postValue(Resource.Loading) }
            .subscribe(
                { value ->
                    currentPage++
                    if(mutableData == null) mutableData = value
                    else mutableData!!.add(value)
                    _isLastPage.postValue(checkIsLastPage(value))
                    _data.postValue(Resource.Success(mutableData ?: value))
                },
                { it.printStackTrace()
                    _data.postValue(Resource.Error(message = it.message ?: "NETWORK_ERROR")) })
            .let(compositeDisposable::add)
    }

    abstract fun getDataSource(): Single<Data>

    abstract fun checkIsLastPage(data: Data): Boolean

    open fun refreshData(){
        mutableData = null
        currentPage = 1
        _isLastPage.postValue(false)
        _data.postValue(Resource.Loading)
        loadPage()
    }
}