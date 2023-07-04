package com.example.data.mapper.image

import com.example.data.database.model.ImageObject
import com.example.data.mapper.Mapper
import com.example.domain.entity.ImageEntity

class ImageObjectToEntityMapper: Mapper<ImageObject, ImageEntity> {
    override fun convert(input: ImageObject): ImageEntity = ImageEntity(id = input.id, name = input.name ?: "")
}