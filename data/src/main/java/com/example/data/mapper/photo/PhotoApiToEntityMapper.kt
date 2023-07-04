package com.example.data.mapper.photo

import com.example.data.api.model.Image
import com.example.data.api.model.Photo
import com.example.data.mapper.Mapper
import com.example.domain.entity.ImageEntity
import com.example.domain.entity.PhotoEntity


class PhotoApiToEntityMapper(private val imageApiToEntityMapper: Mapper<Image, ImageEntity>): Mapper<Photo, PhotoEntity> {
    override fun convert(input: Photo) =
        PhotoEntity(
            id = input.id,
            name = input.name,
            dateCreate = input.dateCreate.substring(0, 10),
            description = input.description,
            new = input.new,
            popular = input.popular,
            userID = input.user?.let { Integer.parseInt(it.substring(11, it.length)) } ?: 1,
            image = input.image?.let { imageApiToEntityMapper.convert(it) } ?: ImageEntity(0, ""),
            isSaved = false
        )
}