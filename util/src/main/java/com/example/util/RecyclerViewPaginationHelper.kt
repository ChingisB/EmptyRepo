package com.example.util


import android.widget.AbsListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Integer.max


class RecyclerViewPaginationHelper(private val loadMore: () -> Unit) : RecyclerView.OnScrollListener() {

    private var endWithAuto = false
    private var threshold = 1
    private var isScrolling = false
    var isLastPage: Boolean = false
    var isLoading: Boolean = false


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (isLastPage || isLoading) return

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

        if (visibleItemCount + lastVisiblePosition + threshold >= totalItemCount && !endWithAuto && isScrolling) {
            endWithAuto = true
            isScrolling = false
            loadMore()
        } else endWithAuto = false
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){ isScrolling = true }
    }

}
