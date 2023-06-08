package com.example.basicapplication.dagger


import android.content.Context
import com.example.basicapplication.MainActivity
import com.example.basicapplication.ui.new_photos.NewPhotosFragment
import com.example.basicapplication.ui.profile.ProfileFragment
import com.example.basicapplication.ui.sign_in.SignInFragment
import com.example.basicapplication.ui.sign_up.SignUpFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [RetrofitModule::class, RepositoryModule::class,
        SharedPreferencesModule::class, TokenManagerModule::class, UseCaseModule::class]
)
interface AppComponent {

    fun injectActivity(activity: MainActivity)

    fun injectSignInFragment(fragment: SignInFragment)

    fun injectSignUpFragment(fragment: SignUpFragment)

    fun injectNewPhotosFragment(fragment: NewPhotosFragment)

    fun injectProfileFragment(fragment: ProfileFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}