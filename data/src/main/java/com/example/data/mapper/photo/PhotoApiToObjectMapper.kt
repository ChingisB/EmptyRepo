package com.example.data.mapper.photo

import com.example.data.Constants
import com.example.data.api.model.Image
import com.example.data.api.model.Photo
import com.example.data.database.model.ImageObject
import com.example.data.database.model.PhotoObject
import com.example.data.mapper.Mapper

class PhotoApiToObjectMapper(private val imageApiToObjectMapper: Mapper<Image, ImageObject>): Mapper<Photo, PhotoObject> {
    override fun convert(input: Photo): PhotoObject {
        return PhotoObject(
            id = input.id,
            dateCreate = input.dateCreate,
            name = input.name,
            description = input.description,
            image = input.image?.let { imageApiToObjectMapper.convert(it) },
            isNew = input.new,
            popular = input.popular,
            userID = Integer.parseInt(input.user?.replace(Constants.USER_ID_PREFIX, "") ?: "1"))
    }
}