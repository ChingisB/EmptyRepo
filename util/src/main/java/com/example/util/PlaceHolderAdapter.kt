package com.example.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PlaceHolderAdapter: RecyclerView.Adapter<PlaceHolderAdapter.PlaceHolder>(){

    private var viewType = ERROR_PLACEHOLDER

    open inner class PlaceHolder(view: View): RecyclerView.ViewHolder(view)

    inner class ErrorPlaceHolder(view: View): PlaceHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder =
        ErrorPlaceHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_error, parent, false))


    override fun getItemCount() = 1

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) = Unit

    override fun getItemViewType(position: Int): Int = viewType


    companion object{
        const val ERROR_PLACEHOLDER = 1
    }
}