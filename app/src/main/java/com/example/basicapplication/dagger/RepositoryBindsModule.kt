package com.example.basicapplication.dagger

import com.example.basicapplication.model.retrofit_model.*
import com.example.basicapplication.data.repository.authentication_repository.AuthenticationRepository
import com.example.basicapplication.data.repository.authentication_repository.AuthenticationRepositoryImpl
import com.example.basicapplication.data.repository.photo_repository.PhotoRepository
import com.example.basicapplication.data.repository.photo_repository.PhotoRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryBindsModule {
    @Binds
    fun bindPhotoRepository(photoRepositoryImpl: PhotoRepositoryImpl): PhotoRepository<Photo, CreatePhoto>

    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository<User, CreateUser, AuthResponse>
}