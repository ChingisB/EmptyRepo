package com.example.basicapplication.util

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class PagingFragment<VBinding: ViewBinding, ViewModel: BaseViewModel>: BaseFragment<VBinding, ViewModel>() {

    abstract val paginator: RecyclerViewPaginator
    abstract val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
}