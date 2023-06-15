package com.example.basicapplication.util


import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class RecyclerViewPaginator : RecyclerView.OnScrollListener() {

    private var currentPage = 1
    private var endWithAuto = false


    abstract var isLastPage: Boolean
    abstract var isLoading: Boolean


    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val layoutManager = recyclerView.layoutManager
        val visibleItemCount = layoutManager!!.childCount
        val totalItemCount = layoutManager.itemCount

        val lastVisiblePosition: Int = when (layoutManager) {
            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        if (isLastPage) {
            return
        }

        if(isLoading){
            return
        }


        if (visibleItemCount + lastVisiblePosition >= totalItemCount) {
            if (!endWithAuto) {
                endWithAuto = true
                loadMore(++currentPage)
            }
        } else {
            endWithAuto = false
        }

    }

    abstract fun loadMore(start: Int)
}
