package com.example.basicapplication.dagger

import androidx.lifecycle.ViewModel
import com.example.basicapplication.SharedImageViewModel
import com.example.basicapplication.SharedPhotoViewModel
import com.example.basicapplication.TokenViewModel
import com.example.basicapplication.ui.favourite.FavouriteViewModel
import com.example.basicapplication.ui.make.MakeViewModel
import com.example.basicapplication.ui.photo_details.PhotoDetailsViewModel
import com.example.basicapplication.ui.photos.PhotosViewModel
import com.example.basicapplication.ui.profile.ProfileViewModel
import com.example.basicapplication.ui.profile_settings.ProfileSettingsViewModel
import com.example.basicapplication.ui.sign_in.SignInViewModel
import com.example.basicapplication.ui.sign_up.SignUpViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @[Binds IntoMap ViewModelKey(FavouriteViewModel::class)]
    fun bindFavouriteViewModel(viewModel: FavouriteViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(MakeViewModel::class)]
    fun bindMakeViewModel(viewModel: MakeViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(PhotoDetailsViewModel::class)]
    fun bindPhotoDetailsViewModel(viewModel: PhotoDetailsViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(PhotosViewModel::class)]
    fun bindPhotosViewModel(viewModel: PhotosViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(ProfileViewModel::class)]
    fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(ProfileSettingsViewModel::class)]
    fun bindProfileSettingsViewModel(viewModel: ProfileSettingsViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(SignInViewModel::class)]
    fun bindSignInViewModel(viewModel: SignInViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(SignUpViewModel::class)]
    fun bindSignUpViewModel(viewModel: SignUpViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(SharedPhotoViewModel::class)]
    fun bindSharedPhotoViewModel(viewModel: SharedPhotoViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(SharedImageViewModel::class)]
    fun bindSharedImageViewModel(viewModel: SharedImageViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(TokenViewModel::class)]
    fun bindTokenViewModel(viewModel: TokenViewModel): ViewModel
}