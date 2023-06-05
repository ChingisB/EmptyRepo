package com.example.basicapplication.dagger


import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.basicapplication.api.Config
import com.example.basicapplication.api.TokenInterceptor
import com.example.basicapplication.api.service.AuthenticationService
import com.example.basicapplication.api.service.PhotoService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module
class RetrofitModule {

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(Config.apiUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService {
        return retrofit.create(AuthenticationService::class.java)
    }

    @Provides
    fun provideOkHttpClient(
        chuckerInterceptor: ChuckerInterceptor, tokenInterceptor: TokenInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(tokenInterceptor)
            .addInterceptor(chuckerInterceptor)
        return okHttpClient.build()
    }

    @Provides
    fun providePhotoService(retrofit: Retrofit): PhotoService {
        return retrofit.create(PhotoService::class.java)
    }

    @Provides
    fun provideChuckerInterceptor(context: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(
                ChuckerCollector(
                    context,
                    showNotification = true,
                    retentionPeriod = RetentionManager.Period.ONE_HOUR
                )
            )
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(true)
            .build()
    }
}