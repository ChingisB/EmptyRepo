package com.example.data.repository.user_repository

import com.example.data.api.model.UpdatePassword
import com.example.data.api.model.UpdateUser
import com.example.data.api.model.User
import com.example.data.api.service.UserService
import com.example.data.mapper.user.UserApiToEntityMapper
import com.example.domain.entity.UserEntity
import com.example.domain.repository.user_repository.RemoteUserRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RemoteUserRepositoryImpl @Inject constructor(private val userService: UserService): RemoteUserRepository {

    private val userApiToEntityMapper = UserApiToEntityMapper()


    override fun updatePassword(id: Int, oldPassword: String, newPassword: String) =
        transformUserResponse(userService.updatePassword(id, UpdatePassword(oldPassword = oldPassword, newPassword = newPassword)))

    override fun updateUser(id: Int, email: String, birthday: String, username: String) =
        transformUserResponse(userService.updateUser(id, UpdateUser(email = email, birthday = birthday, username = username)))

    override fun deleteUser(id: Int): Completable {
        return Completable.fromAction { userService.deleteUser(id) }.observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun getCurrentUser() = transformUserResponse(userService.getCurrentUser())

    private fun transformUserResponse(response: Single<User>): Single<UserEntity>{
        return response.map(userApiToEntityMapper::convert).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}