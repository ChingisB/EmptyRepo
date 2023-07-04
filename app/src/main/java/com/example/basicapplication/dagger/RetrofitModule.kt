package com.example.basicapplication.dagger


import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.data.api.AuthenticatorImpl
import com.example.data.api.Config
import com.example.data.api.TokenInterceptor
import com.example.data.api.service.AuthenticationService
import com.example.data.api.service.ImageService
import com.example.data.api.service.PhotoService
import com.example.data.api.service.UserService
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module
interface RetrofitModule {

    @Binds
    fun bindAuthenticator(authenticator: AuthenticatorImpl): Authenticator


    companion object {
        @Provides
        fun provideGsonAdapterFactory(): GsonConverterFactory = GsonConverterFactory.create()

        @Provides
        fun provideCallAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

        @Provides
        fun provideChuckerCollector(context: Context): ChuckerCollector {
            return ChuckerCollector(context, showNotification = true, retentionPeriod = RetentionManager.Period.ONE_HOUR)
        }

        @Provides
        fun provideChuckerInterceptor(
            context: Context,
            chuckerCollector: ChuckerCollector
        ): ChuckerInterceptor {
            return ChuckerInterceptor.Builder(context)
                .collector(chuckerCollector)
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(true)
                .build()
        }

        @Provides
        fun provideOkHttpClient(
            chuckerInterceptor: ChuckerInterceptor,
            authenticator: Authenticator,
            tokenInterceptor: TokenInterceptor
        ): OkHttpClient {
            val okHttpClient =
                OkHttpClient.Builder().authenticator(authenticator).addInterceptor(tokenInterceptor).addInterceptor(chuckerInterceptor)
            return okHttpClient.build()
        }

        @Provides
        fun provideRetrofit(
            okHttpClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory,
            rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
        ): Retrofit {
            return Retrofit.Builder().baseUrl(Config.API_URL).client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build()
        }

        @Provides
        fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService = retrofit.create(AuthenticationService::class.java)

        @Provides
        fun providePhotoService(retrofit: Retrofit): PhotoService = retrofit.create(PhotoService::class.java)

        @Provides
        fun provideUserService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)

        @Provides
        fun provideImageService(retrofit: Retrofit): ImageService = retrofit.create(ImageService::class.java)

    }

}