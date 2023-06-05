package com.example.basicapplication.dagger

import com.example.basicapplication.model.retrofitModel.*
import com.example.basicapplication.repository.authenticationRepository.AuthenticationRepository
import com.example.basicapplication.repository.authenticationRepository.AuthenticationRepositoryImpl
import com.example.basicapplication.repository.photoRepository.PhotoRepository
import com.example.basicapplication.repository.photoRepository.PhotoRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryBindsModule {
    @Binds
    fun bindPhotoRepository(photoRepositoryImpl: PhotoRepositoryImpl): PhotoRepository<Photo, CreatePhoto>

    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository<User, CreateUser, AuthResponse>
}