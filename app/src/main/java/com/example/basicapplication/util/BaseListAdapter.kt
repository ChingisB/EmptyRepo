package com.example.basicapplication.util

import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<Data, VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {


    abstract fun submitList(data: List<Data>)

    abstract fun clearList()

}