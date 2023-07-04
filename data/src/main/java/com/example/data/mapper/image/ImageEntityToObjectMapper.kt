package com.example.data.mapper.image

import com.example.data.database.model.ImageObject
import com.example.data.mapper.Mapper
import com.example.domain.entity.ImageEntity

class ImageEntityToObjectMapper: Mapper<ImageEntity, ImageObject> {
    override fun convert(input: ImageEntity): ImageObject = ImageObject(input.id, input.name)

}