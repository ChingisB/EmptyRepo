package com.example.basicapplication.base

import android.content.pm.ActivityInfo
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.viewbinding.ViewBinding
import com.example.basicapplication.util.BaseListAdapter
import com.example.basicapplication.util.MarginItemDecoration
import com.example.basicapplication.util.RecyclerViewPaginationHelper
import com.example.basicapplication.util.Resource

//TODO refactor genericks

abstract class PagingFragment<
        VBinding : ViewBinding,
        Data,
        ViewModel : PagingViewModel<Data>,
        ListAdapter : BaseListAdapter<Data, *>> :
    BaseFragment<VBinding, ViewModel>() {

    private val paginationHelper: RecyclerViewPaginationHelper = RecyclerViewPaginationHelper { viewModel.loadPage() }
    abstract var listAdapter: ListAdapter
    abstract val spanCount: Int
    abstract val spaceSize: Int
    private lateinit var marginItemDecoration: MarginItemDecoration

    override fun observeData() {
//        TODO optimize
        viewModel.data.observe(viewLifecycleOwner) {
            paginationHelper.isLoading = when (it) {
                is Resource.Success-> {
                    it.data?.let {it1 -> listAdapter.submitList(it1) }
                    false
                }
                is Resource.Loading -> true
                is Resource.Error -> {
                    showPageError(it)
                    false
                }
            }
            changePageLoadingState(paginationHelper.isLoading)
        }

        viewModel.isLastPage.observe(viewLifecycleOwner) {
            paginationHelper.isLastPage = it
        }
    }

    //        TODO optimize
    //private fun createPaginator() = Unit

    fun resetPagination(query: String?) {
        viewModel.query = query ?: ""
        listAdapter.clearList()
        paginationHelper.reset()
        viewModel.page = 1
        viewModel.loadPage()
    }

    abstract fun createListAdapter(): ListAdapter

    open fun createLayoutManager(): LayoutManager {
        val context = requireContext()
        if (context.resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return GridLayoutManager(context, spanCount)
        }
        return GridLayoutManager(context, spanCount * 2)
    }

//    TODO trash methods
    abstract fun changePageLoadingState(isLoading: Boolean)

    abstract fun showPageError(error: Resource.Error<List<Data>>)

    private fun createMarginItemDecoration(): MarginItemDecoration {
        return MarginItemDecoration(spaceSize, spanCount)
    }

    fun setupRecyclerView(recyclerView: RecyclerView) {
        listAdapter = createListAdapter()
        recyclerView.adapter = null
        recyclerView.layoutManager = null
        recyclerView.clearOnScrollListeners()
        marginItemDecoration = createMarginItemDecoration()
        recyclerView.apply {
            adapter = listAdapter
            layoutManager = createLayoutManager()
            addOnScrollListener(paginationHelper)
            if(itemDecorationCount == 0) addItemDecoration(marginItemDecoration)

        }
    }
}