package com.example.basicapplication.util


import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Integer.max


class RecyclerViewPaginationHelper(private val loadMore: () -> Unit) : RecyclerView.OnScrollListener() {

    private var endWithAuto = false
    private var threshold = 1
    var isLastPage: Boolean = false
    var isLoading: Boolean = false


    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val layoutManager = recyclerView.layoutManager
        val visibleItemCount = layoutManager!!.childCount
        val totalItemCount = layoutManager.itemCount

        val lastVisiblePosition: Int = when (layoutManager) {
            is GridLayoutManager -> {
                threshold = max(layoutManager.spanCount, 1)
                layoutManager.findLastVisibleItemPosition()
            }
            else -> (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        if (isLastPage || isLoading) return

        if (visibleItemCount + lastVisiblePosition + threshold >= totalItemCount && !endWithAuto) {
            endWithAuto = true
            loadMore()
        } else endWithAuto = false
    }

    fun reset() {
        isLastPage = false
        loadMore.invoke()
    }
}
