package com.example.data.mapper.photo

import com.example.data.database.model.PhotoObject
import com.example.data.mapper.Mapper
import com.example.data.mapper.image.ImageObjectToEntityMapper
import com.example.domain.entity.ImageEntity
import com.example.domain.entity.PhotoEntity

class PhotoObjectToEntityMapper(private val imageObjectToEntityMapper: ImageObjectToEntityMapper): Mapper<PhotoObject, PhotoEntity> {
    override fun convert(input: PhotoObject) =
        PhotoEntity(
            id = input.id,
            name = input.name ?: "",
            dateCreate = input.dateCreate ?: "",
            description = input.description ?: "",
            new = input.isNew ?: false,
            popular = input.popular ?: false,
            userID = input.userID ?: 1,
            image = input.image?.let { imageObjectToEntityMapper.convert(it) } ?: ImageEntity(0, ""),
            isSaved = input.isSaved ?: false
        )
}
