package com.example.basicapplication

import android.app.Application
import com.example.basicapplication.dagger.AppComponent
import com.example.basicapplication.dagger.DaggerAppComponent

class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

    companion object{
        lateinit var appComponent: AppComponent
    }
}
