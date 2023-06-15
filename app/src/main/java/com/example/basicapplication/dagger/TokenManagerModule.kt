package com.example.basicapplication.dagger

import com.example.basicapplication.data.repository.token_repository.TokenRepository
import com.example.basicapplication.data.repository.token_repository.TokenRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface TokenManagerModule {

    @Binds
    fun bindTokenManager(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository
}