package com.example.basicapplication.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE


abstract class RecyclerViewPaginator(recyclerView: RecyclerView) : RecyclerView.OnScrollListener() {
    private var currentPage = 1
    private var endWithAuto = false


    private var layoutManager: RecyclerView.LayoutManager? = null


    private val startPage: Int
        get() = ++currentPage


    abstract val isLastPage: Boolean


    init {
        recyclerView.addOnScrollListener(this)
        layoutManager = recyclerView.layoutManager
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == SCROLL_STATE_IDLE) {


            val visibleItemCount = layoutManager!!.childCount
            val totalItemCount = layoutManager!!.itemCount

            val firstVisiblePosition: Int = when (layoutManager) {
                is GridLayoutManager -> (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                is LinearLayoutManager -> (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                else -> 0
            }

            if (isLastPage) {
                return
            }


            if (visibleItemCount + firstVisiblePosition >= totalItemCount) {
                if (!endWithAuto) {
                    endWithAuto = true
                    loadMore(startPage)
                } else {
                    endWithAuto = false
                }
            }
            else{
                endWithAuto = false
            }
        }
    }

    fun reset() {
        currentPage = 0
    }

    abstract fun loadMore(start: Int)
}
