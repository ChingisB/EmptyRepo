package com.example.data.mapper.user

import com.example.data.database.model.UserObject
import com.example.data.mapper.Mapper
import com.example.domain.entity.UserEntity

class UserObjectToEntityMapper: Mapper<UserObject, UserEntity> {
    override fun convert(input: UserObject): UserEntity =
        UserEntity(id = input.id, username = input.username ?: "", email = input.email ?: "", birthday = input.birthday ?: "")
}