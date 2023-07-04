package com.example.basicapplication.dagger


import com.example.basicapplication.util.AndroidResourceProvider
import com.example.basicapplication.util.PatternsEmailMatcher
import com.example.domain.email_matcher.EmailMatcher
import com.example.domain.repository.photo_repository.LocalPhotoRepository
import com.example.domain.repository.photo_repository.RemotePhotoRepository
import com.example.domain.repository.token_repository.TokenRepository
import com.example.domain.repository.user_repository.LocalUserRepository
import com.example.domain.repository.user_repository.RemoteUserRepository
import com.example.domain.resource_provider.ResourceProvider
import com.example.domain.use_case.*
import com.example.domain.use_case.validation.ValidateBirthdayUseCase
import com.example.domain.use_case.validation.ValidateConfirmPasswordUseCase
import com.example.domain.use_case.validation.ValidateEmailUseCase
import com.example.domain.use_case.validation.ValidatePasswordUseCase
import com.example.domain.use_case.validation.ValidateUsernameUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface UseCaseModule {

    @Binds
    fun bindEmailMatcher(patternsEmailMatcher: PatternsEmailMatcher): EmailMatcher

    @Binds
    fun bindResourceProvider(androidResourceProvider: AndroidResourceProvider): ResourceProvider

    companion object {
        @Provides
        fun provideValidateUsernameUseCase(resourceProvider: ResourceProvider) = ValidateUsernameUseCase(resourceProvider)

        @Provides
        fun provideValidateEmailUseCase(emailMatcher: EmailMatcher, resourceProvider: ResourceProvider) =
            ValidateEmailUseCase(emailMatcher, resourceProvider)

        @Provides
        fun provideValidateBirthdayUseCase(parseDateUseCase: ParseDateUseCase, resourceProvider: ResourceProvider) =
            ValidateBirthdayUseCase(parseDateUseCase, resourceProvider)

        @Provides
        fun provideParseDateUseCase() = ParseDateUseCase("yyyy-MM-dd")

        @Provides
        fun provideValidatePasswordUseCase(resourceProvider: ResourceProvider) = ValidatePasswordUseCase(resourceProvider)

        @Provides
        fun provideValidateConfirmPasswordUseCase(resourceProvider: ResourceProvider) = ValidateConfirmPasswordUseCase(resourceProvider)

        @Provides
        fun providePatternsEmailMatcher() = PatternsEmailMatcher()

        @Provides
        fun provideConvertLocalDateUseCase() = ConvertLocalDateUseCase()

        @Provides
        fun provideGetPhotosUseCase(localPhotoRepository: LocalPhotoRepository, remotePhotoRepository: RemotePhotoRepository) =
            GetPhotosUseCase(localPhotoRepository, remotePhotoRepository)

        @Provides
        fun provideGetUserPhotosUseCase(localPhotoRepository: LocalPhotoRepository, remotePhotoRepository: RemotePhotoRepository) =
            GetUserPhotosUseCase(localPhotoRepository, remotePhotoRepository)

        @Provides
        fun provideGetCurrentUserUseCase(localUserRepository: LocalUserRepository, remoteUserRepository: RemoteUserRepository) =
            GetCurrentUserUseCase(localUserRepository, remoteUserRepository)

        @Provides
        fun provideSignOutUseCase(localPhotoRepository: LocalPhotoRepository,
                                  localUserRepository: LocalUserRepository,
                                  tokenRepository: TokenRepository
        ) = SignOutUseCase(localPhotoRepository, localUserRepository, tokenRepository)

    }

}