package com.example.basicapplication.data.repository.user_repository

import com.example.basicapplication.data.data_source.api.service.UserService
import com.example.basicapplication.data.model.Photo
import com.example.basicapplication.data.model.UpdatePassword
import com.example.basicapplication.data.model.UpdateUser
import com.example.basicapplication.data.model.User
import com.example.basicapplication.data.repository.photo_repository.PhotoRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val photoRepository: PhotoRepository
) :
    UserRepository {
    override fun updatePassword(id: Int, updatePassword: UpdatePassword): Single<User> =
        userService.updatePassword(id, updatePassword).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    override fun updateUser(id: Int, updateUser: UpdateUser): Single<User> {
        return userService.updateUser(id, updateUser)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    override fun deleteUser(id: Int): Completable =
        userService.deleteUser(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    override fun getCurrentUser(): Single<User> =
        userService.getCurrentUser().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    override fun getUserPhotos(userId: Int, page: Int): Single<List<Photo>> {
        return photoRepository.getUserPhotos(userId, page)
    }
}