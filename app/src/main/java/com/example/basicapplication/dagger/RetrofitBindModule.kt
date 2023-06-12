package com.example.basicapplication.dagger

import com.example.basicapplication.data.data_source.api.AuthenticatorImpl
import com.example.basicapplication.data.data_source.api.TokenManager
import com.example.basicapplication.data.data_source.api.TokenManagerImpl
import dagger.Binds
import dagger.Module
import okhttp3.Authenticator

@Module
interface RetrofitBindModule {

    @Binds
    fun bindAuthenticator(authenticator: AuthenticatorImpl): Authenticator


}