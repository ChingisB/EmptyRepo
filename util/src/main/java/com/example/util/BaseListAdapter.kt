package com.example.util

import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<in Data, VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {


    abstract fun submitData(data: Data)

    abstract fun clear()

}