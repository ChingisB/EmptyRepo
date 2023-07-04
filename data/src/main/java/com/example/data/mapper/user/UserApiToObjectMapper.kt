package com.example.data.mapper.user

import com.example.data.api.model.User
import com.example.data.database.model.UserObject
import com.example.data.mapper.Mapper

class UserApiToObjectMapper: Mapper<User, UserObject> {
    override fun convert(input: User): UserObject =
        UserObject(id = input.id, username = input.username, email = input.email, birthday = input.birthday)
}