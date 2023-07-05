package com.example.basicapplication.dagger

import android.content.Context
import android.content.SharedPreferences
import com.example.data.Constants
import com.example.data.FileFromUriResolver
import com.example.data.FileFromUriResolverImpl
import com.example.data.repository.authentication_repository.AuthenticationRepository
import com.example.data.repository.authentication_repository.AuthenticationRepositoryImpl
import com.example.data.repository.photo_repository.LocalPhotoRepositoryImpl
import com.example.data.repository.photo_repository.RemotePhotoRepositoryImpl
import com.example.domain.repository.token_repository.TokenRepository
import com.example.data.repository.token_repository.TokenRepositoryImpl
import com.example.data.repository.user_repository.LocalUserRepositoryImpl
import com.example.data.repository.user_repository.RemoteUserRepositoryImpl
import com.example.domain.repository.photo_repository.LocalPhotoRepository
import com.example.domain.repository.photo_repository.RemotePhotoRepository
import com.example.domain.repository.user_repository.LocalUserRepository
import com.example.domain.repository.user_repository.RemoteUserRepository
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface RepositoryModule {

    @Binds
    fun bindLocalPhotoRepository(localPhotoRepository: LocalPhotoRepositoryImpl): LocalPhotoRepository

    @Binds
    fun bindRemotePhotoRepository(remotePhotoRepository: RemotePhotoRepositoryImpl): RemotePhotoRepository

    @Binds
    fun bindLocalUserRepository(localUserRepository: LocalUserRepositoryImpl): LocalUserRepository

    @Binds
    fun bindRemoteUserRepository(remoteUserRepository: RemoteUserRepositoryImpl): RemoteUserRepository

    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository

    @Binds
    fun bindTokenRepository(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository

    @Binds
    fun bindFileFromUriResolver(fileFromUriResolverImpl: FileFromUriResolverImpl): FileFromUriResolver


    companion object{
        @Provides
        fun provideSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(Constants.PREFERENCES_KEY, Context.MODE_PRIVATE)
        }

        @Provides
        fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    }

}