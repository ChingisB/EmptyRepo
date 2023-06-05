package com.example.basicapplication.dagger


import android.content.Context
import com.example.basicapplication.MainActivity
import com.example.basicapplication.ui.newPhotos.NewPhotosFragment
import com.example.basicapplication.ui.signIn.SignInFragment
import com.example.basicapplication.ui.signUp.SignUpFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [RetrofitModule::class, RepositoryModule::class, SharedPreferencesModule::class])
interface AppComponent{

    fun injectActivity(activity: MainActivity)

    fun injectSignInFragment(fragment: SignInFragment)

    fun injectSignUpFragment(fragment: SignUpFragment)

    fun injectNewPhotosFragment(fragment: NewPhotosFragment)

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}