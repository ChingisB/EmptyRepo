package com.example.base

import android.content.pm.ActivityInfo
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.viewbinding.ViewBinding
import com.example.domain.MutableData
import com.example.util.*

// TODO refactor generics

abstract class PagingFragment<
        VBinding: ViewBinding,
        Data: MutableData,
        ViewModel : PagingViewModel<Data>,
        ListAdapter : BaseListAdapter<Data, *>>:
    BaseFragment<VBinding, ViewModel>() {

    abstract val spanCount: Int
    abstract val spaceSize: Int
    val listAdapter: ListAdapter by lazy { createListAdapter() }
    private val paginationHelper: RecyclerViewPaginationHelper = RecyclerViewPaginationHelper { viewModel.loadPage() }
    private var isErrorShowing = false
    private lateinit var marginItemDecoration: MarginItemDecoration

    abstract fun createListAdapter(): ListAdapter

    open fun createLayoutManager(): LayoutManager {
        if (requireContext().resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return GridLayoutManager(requireContext(), spanCount)
        }
        return GridLayoutManager(requireContext(), spanCount * 2)
    }

    private fun createMarginItemDecoration() = MarginItemDecoration(spaceSize, spanCount)

    fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = null
        recyclerView.layoutManager = null
        recyclerView.clearOnScrollListeners()
        marginItemDecoration = createMarginItemDecoration()
        recyclerView.apply {
            adapter = listAdapter
            layoutManager = createLayoutManager()
            addOnScrollListener(paginationHelper)
            if (itemDecorationCount == 0) addItemDecoration(marginItemDecoration)
        }
    }

    override fun observeData() {
//        TODO optimize
        viewModel.data.observe(viewLifecycleOwner) {
            paginationHelper.isLoading = when (it) {
                is Resource.Loading -> true
                is Resource.Success -> {
                    if(isErrorShowing){
                        isErrorShowing = false
                        showPage()
                    }
                    listAdapter.submitData(it.data)
                    false
                }
                is Resource.Error -> {
                    isErrorShowing = true
                    Log.e("what is the error", it.message)
                    showPageError(it)
                    false
                }
            }
            changePageLoadingState(paginationHelper.isLoading)
        }

        viewModel.isLastPage.observe(viewLifecycleOwner) { paginationHelper.isLastPage = it }
    }
    //        TODO optimize
    //private fun createPaginator() = Unit

//    TODO trash methods

    abstract fun changePageLoadingState(isLoading: Boolean)

    abstract fun showPageError(error: Resource.Error)

    abstract fun showPage()
}