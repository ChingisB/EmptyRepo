package com.example.basicapplication.dagger


import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.basicapplication.data.data_source.api.AuthenticatorImpl
import com.example.basicapplication.data.data_source.api.Config
import com.example.basicapplication.data.data_source.api.TokenInterceptor
import com.example.basicapplication.data.data_source.api.TokenManager
import com.example.basicapplication.data.data_source.api.service.AuthenticationService
import com.example.basicapplication.data.data_source.api.service.PhotoService
import com.example.basicapplication.data.data_source.api.service.UserService
import dagger.Module
import dagger.Provides
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider


@Module(includes = [RetrofitBindModule::class])
class RetrofitModule {

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder().baseUrl(Config.apiUrl).client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .build()
    }

    @Provides
    fun provideAuthenticatorImpl(tokenManager: TokenManager, authenticationServiceProvider: Provider<AuthenticationService>): AuthenticatorImpl{
        return AuthenticatorImpl(authenticationServiceProvider, tokenManager)
    }

    @Provides
    fun provideGsonAdapterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideCallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService {
        return retrofit.create(AuthenticationService::class.java)
    }

    @Provides
    fun provideOkHttpClient(
        chuckerInterceptor: ChuckerInterceptor,
        authenticator: Authenticator,
        tokenInterceptor: TokenInterceptor
    ): OkHttpClient {
        val okHttpClient =
            OkHttpClient.Builder().authenticator(authenticator).addInterceptor(tokenInterceptor)
                .addInterceptor(chuckerInterceptor)
        return okHttpClient.build()
    }

    @Provides
    fun providePhotoService(retrofit: Retrofit): PhotoService {
        return retrofit.create(PhotoService::class.java)
    }

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    fun provideChuckerCollector(context: Context): ChuckerCollector {
        return ChuckerCollector(
            context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
    }

    @Provides
    fun provideChuckerInterceptor(
        context: Context,
        chuckerCollector: ChuckerCollector
    ): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(
                chuckerCollector
            )
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(true)
            .build()
    }
}