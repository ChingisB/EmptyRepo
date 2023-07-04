package com.example.data.mapper.image

import com.example.data.api.model.Image
import com.example.data.database.model.ImageObject
import com.example.data.mapper.Mapper

class ImageApiToObjectMapper: Mapper<Image, ImageObject> {
    override fun convert(input: Image): ImageObject = ImageObject(id = input.id, name = input.name)
}