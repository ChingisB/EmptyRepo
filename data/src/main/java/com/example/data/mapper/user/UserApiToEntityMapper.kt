package com.example.data.mapper.user

import com.example.data.api.model.User
import com.example.data.mapper.Mapper
import com.example.domain.entity.UserEntity

class UserApiToEntityMapper: Mapper<User, UserEntity> {
    override fun convert(input: User): UserEntity =
        UserEntity(id = input.id, username = input.username, email = input.email, birthday = input.birthday?.substring(0, 10) ?: "")
}