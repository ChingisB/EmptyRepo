package com.example.basicapplication.dagger

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesModule {

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("gallery", Context.MODE_PRIVATE)
    }
}