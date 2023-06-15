package com.example.basicapplication.data.repository.user_repository

import com.example.basicapplication.data.data_source.api.service.UserService
import com.example.basicapplication.model.retrofit_model.UpdatePassword
import com.example.basicapplication.model.retrofit_model.UpdateUser
import com.example.basicapplication.model.retrofit_model.User
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userService: UserService) :
    UserRepository<User, UpdateUser, UpdatePassword> {
    override fun updatePassword(id: Int, updatePassword: UpdatePassword): Single<User> =
        userService.updatePassword(id, updatePassword).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    override fun updateUser(id: Int, updateUser: UpdateUser): Single<User> =
        userService.updateUser(id, updateUser).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    override fun deleteUser(id: Int): Completable =
        userService.deleteUser(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    override fun getCurrentUser(): Single<User> =
        userService.getCurrentUser().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}