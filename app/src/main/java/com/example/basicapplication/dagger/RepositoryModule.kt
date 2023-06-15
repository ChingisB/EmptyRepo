package com.example.basicapplication.dagger

import android.content.Context
import android.content.SharedPreferences
import com.example.basicapplication.data.repository.token_repository.TokenRepository
import com.example.basicapplication.data.repository.token_repository.TokenRepositoryImpl
import com.example.basicapplication.data.repository.authentication_repository.AuthenticationRepository
import com.example.basicapplication.data.repository.authentication_repository.AuthenticationRepositoryImpl
import com.example.basicapplication.data.repository.photo_repository.PhotoRepository
import com.example.basicapplication.data.repository.photo_repository.PhotoRepositoryImpl
import com.example.basicapplication.data.repository.user_repository.UserRepository
import com.example.basicapplication.data.repository.user_repository.UserRepositoryImpl
import com.example.basicapplication.model.retrofit_model.*
import com.example.basicapplication.util.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface RepositoryModule {

    @Binds
    fun bindPhotoRepository(photoRepositoryImpl: PhotoRepositoryImpl):
            PhotoRepository<Photo, CreatePhoto>

    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthenticationRepositoryImpl):
            AuthenticationRepository<User, CreateUser, AuthResponse>

    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl):
            UserRepository<User, UpdateUser, UpdatePassword>

    @Binds
    fun bindTokenRepository(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository


    companion object{
        @Provides
        fun provideSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(Constants.preferencesKey, Context.MODE_PRIVATE)
        }
    }


}