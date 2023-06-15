package com.example.basicapplication.dagger

import com.example.basicapplication.data.data_source.api.AuthenticatorImpl
import dagger.Binds
import dagger.Module
import okhttp3.Authenticator

@Module
interface RetrofitBindModule {

    @Binds
    fun bindAuthenticator(authenticator: AuthenticatorImpl): Authenticator


}