package com.example.data.mapper.user

import com.example.data.database.model.UserObject
import com.example.data.mapper.Mapper
import com.example.domain.entity.UserEntity

class UserEntityToObjectMapper: Mapper<UserEntity, UserObject> {
    override fun convert(input: UserEntity) =
        UserObject(id = input.id, email = input.email, username = input.username, birthday = input.birthday)

}