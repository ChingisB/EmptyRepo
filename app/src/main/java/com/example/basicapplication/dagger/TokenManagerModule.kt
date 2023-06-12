package com.example.basicapplication.dagger

import com.example.basicapplication.data.data_source.api.TokenManager
import com.example.basicapplication.data.data_source.api.TokenManagerImpl
import dagger.Binds
import dagger.Module

@Module
interface TokenManagerModule {

    @Binds
    fun bindTokenManager(tokenManagerImpl: TokenManagerImpl): TokenManager
}