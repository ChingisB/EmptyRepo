package com.example.data.mapper.photo

import com.example.data.database.model.PhotoObject
import com.example.data.mapper.Mapper
import com.example.data.mapper.image.ImageEntityToObjectMapper
import com.example.domain.entity.PhotoEntity

class PhotoEntityToObjectMapper(private val imageEntityToObjectMapper: ImageEntityToObjectMapper) : Mapper<PhotoEntity, PhotoObject> {
    override fun convert(input: PhotoEntity): PhotoObject =
        PhotoObject(
            id = input.id,
            dateCreate = input.dateCreate,
            name = input.name,
            description = input.description,
            image = imageEntityToObjectMapper.convert(input.image),
            popular = input.popular,
            userID = input.userID,
            isNew = input.new,
            isSaved = input.isSaved
        )
}