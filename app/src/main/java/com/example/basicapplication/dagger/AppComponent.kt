package com.example.basicapplication.dagger


import android.content.Context
import com.example.basicapplication.MainActivity
import com.example.basicapplication.ui.new_photos.NewPhotosFragment
import com.example.basicapplication.ui.popular_photos.PopularPhotosFragment
import com.example.basicapplication.ui.profile.ProfileFragment
import com.example.basicapplication.ui.profile_settings.ProfileSettingsFragment
import com.example.basicapplication.ui.sign_in.SignInFragment
import com.example.basicapplication.ui.sign_up.SignUpFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        RetrofitModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: SignInFragment)

    fun inject(fragment: SignUpFragment)

    fun inject(fragment: NewPhotosFragment)

    fun inject(fragment: ProfileFragment)

    fun inject(fragment: ProfileSettingsFragment)

    fun inject(fragment: PopularPhotosFragment)

//    TODO change builder to factory
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}