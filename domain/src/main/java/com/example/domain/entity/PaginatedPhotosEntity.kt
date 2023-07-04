package com.example.domain.entity


import com.example.domain.MutableData


data class PaginatedPhotosEntity(
    val countOfPages: Int,
    val data: MutableList<PhotoEntity>,
    val itemsPerPage: Long,
    val totalItems: Int
): MutableData{
    override fun add(other: MutableData) {
        if(other is PaginatedPhotosEntity) this.data.addAll(other.data)
    }

}