package com.example.basicapplication.data.repository.user_repository

import com.example.basicapplication.model.base_model.UpdatePasswordInterface
import com.example.basicapplication.model.base_model.UpdateUserInterface
import com.example.basicapplication.model.base_model.UserInterface
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository<T: UserInterface, R: UpdateUserInterface, K: UpdatePasswordInterface>{

    fun updatePassword(id: Int, updatePassword: K): Single<T>

    fun updateUser(id: Int, updateUser: R): Single<T>

    fun deleteUser(id: Int): Completable

    fun getCurrentUser(): Single<T>
}