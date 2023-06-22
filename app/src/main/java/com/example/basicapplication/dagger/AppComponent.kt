package com.example.basicapplication.dagger


import android.content.Context
import com.example.basicapplication.MainActivity
import com.example.basicapplication.ui.bottom_sheet_dialog_fragment.BottomSheetDialog
import com.example.basicapplication.ui.make.MakeFragment
import com.example.basicapplication.ui.photos.PhotosFragment
import com.example.basicapplication.ui.profile.ProfileFragment
import com.example.basicapplication.ui.profile_settings.ProfileSettingsFragment
import com.example.basicapplication.ui.sign_in.SignInFragment
import com.example.basicapplication.ui.sign_up.SignUpFragment
import com.example.basicapplication.ui.welcome.WelcomeFragment
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

    fun inject(fragment: PhotosFragment)

    fun inject(fragment: ProfileFragment)

    fun inject(fragment: ProfileSettingsFragment)

    fun inject(fragment: WelcomeFragment)

    fun inject(fragment: MakeFragment)

    fun inject(dialog: BottomSheetDialog)
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}