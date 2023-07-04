package com.example.data.database.model


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class ImageObject(
    @PrimaryKey var id: Int = 0,
    var name: String? = null
): RealmObject()