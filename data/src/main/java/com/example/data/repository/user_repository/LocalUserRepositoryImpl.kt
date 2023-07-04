package com.example.data.repository.user_repository

import com.example.data.SingleThreadScheduler
import com.example.data.database.model.UserObject
import com.example.data.mapper.user.UserEntityToObjectMapper
import com.example.data.mapper.user.UserObjectToEntityMapper
import com.example.domain.entity.UserEntity
import com.example.domain.repository.user_repository.LocalUserRepository
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.kotlin.delete
import io.realm.kotlin.where
import javax.inject.Inject

class LocalUserRepositoryImpl @Inject constructor(
    private val realm: Realm,
    @SingleThreadScheduler private val singleThreadScheduler: Scheduler
): LocalUserRepository {

    private val userObjectToEntityMapper = UserObjectToEntityMapper()
    private val userEntityToObjectMapper = UserEntityToObjectMapper()


    override fun insertUser(userEntity: UserEntity): Completable {
        return Completable.fromAction { realm.executeTransaction { r -> r.insertOrUpdate(userEntityToObjectMapper.convert(userEntity)) } }
            .subscribeOn(singleThreadScheduler)
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteLocalUser(): Completable = deleteUser(0)

    override fun deleteUser(id: Int): Completable =
        Completable.fromAction { realm.delete<UserObject>() }.subscribeOn(singleThreadScheduler).observeOn(AndroidSchedulers.mainThread())


    override fun getCurrentUser(): Single<UserEntity> {
        return Single.fromCallable { realm.where<UserObject>().findFirst()?.let {userObjectToEntityMapper.convert(it)} }
            .subscribeOn(singleThreadScheduler)
            .observeOn(AndroidSchedulers.mainThread())
    }
}