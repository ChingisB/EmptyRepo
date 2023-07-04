package com.example.domain.use_case

import com.example.domain.repository.photo_repository.LocalPhotoRepository
import com.example.domain.repository.token_repository.TokenRepository
import com.example.domain.repository.user_repository.LocalUserRepository
import io.reactivex.Completable
class SignOutUseCase(
    private val localPhotoRepository: LocalPhotoRepository,
    private val localUserRepository: LocalUserRepository,
    private val tokenRepository: TokenRepository
) {
    operator fun invoke(): Completable {
        tokenRepository.deleteTokens()
        return localPhotoRepository.clearLocalPhotos().andThen { localUserRepository.deleteLocalUser() }
    }
}