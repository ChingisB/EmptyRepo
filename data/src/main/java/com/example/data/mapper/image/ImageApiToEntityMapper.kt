package com.example.data.mapper.image

import com.example.data.api.model.Image
import com.example.data.mapper.Mapper
import com.example.domain.entity.ImageEntity

class ImageApiToEntityMapper: Mapper<Image, ImageEntity> {
    override fun convert(input: Image): ImageEntity = ImageEntity(id = input.id, name = input.name)
}