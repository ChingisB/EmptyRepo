package com.example.domain.use_case

import com.example.domain.entity.UserEntity
import com.example.domain.repository.user_repository.RemoteUserRepository
import io.reactivex.Single

class GetUserUseCase(private val remoteUserRepository: RemoteUserRepository) {
    operator fun invoke(userId: Int): Single<UserEntity> = remoteUserRepository.getUser(userId)
}