package com.example.domain.use_case

import com.example.domain.entity.UserEntity
import com.example.domain.repository.user_repository.LocalUserRepository
import com.example.domain.repository.user_repository.RemoteUserRepository
import io.reactivex.Single

class GetCurrentUserUseCase(private val localUserRepository: LocalUserRepository, private val remoteUserRepository: RemoteUserRepository) {
    operator fun invoke(): Single<UserEntity> =
        remoteUserRepository.getCurrentUser().flatMap {
            localUserRepository.insertUser(it).andThen(localUserRepository.getCurrentUser())
        }.onErrorResumeNext {  localUserRepository.getCurrentUser() }

}