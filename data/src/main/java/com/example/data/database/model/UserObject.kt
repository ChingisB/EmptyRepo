package com.example.data.database.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserObject(
    @PrimaryKey var id: Int = 0,
    var email: String? = null,
    var username: String? = null,
    var birthday: String? = null
): RealmObject()