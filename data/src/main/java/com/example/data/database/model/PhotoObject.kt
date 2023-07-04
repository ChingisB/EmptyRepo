package com.example.data.database.model


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class PhotoObject(
    @PrimaryKey var id: Int = 0,
    var dateCreate: String? = null,
    var name: String? = null,
    var description: String? = null,
    var image: ImageObject? = null,
    var popular: Boolean? = null,
    var userID: Int? = null,
    var isNew: Boolean = false,
    var isSaved: Boolean? = null
): RealmObject()