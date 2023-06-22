package com.example.basicapplication.data.repository.user_repository


import com.example.basicapplication.data.model.Photo
import com.example.basicapplication.data.model.UpdatePassword
import com.example.basicapplication.data.model.UpdateUser
import com.example.basicapplication.data.model.User
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository{

    fun updatePassword(id: Int, updatePassword: UpdatePassword): Single<User>

    fun updateUser(id: Int, updateUser: UpdateUser): Single<User>

    fun deleteUser(id: Int): Completable

    fun getCurrentUser(): Single<User>

    fun getUserPhotos(userId: Int, page: Int): Single<List<Photo>>
}