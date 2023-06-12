package com.example.basicapplication.dagger

import com.example.basicapplication.domain.use_case.*
import com.example.basicapplication.util.EmailMatcher
import com.example.basicapplication.util.PatternsEmailMatcher
import dagger.Module
import dagger.Provides

@Module(includes = [UseCaseBindsModule::class])
class UseCaseModule {
    @Provides
    fun provideValidateUsernameUseCase() = ValidateUsernameUseCase()

    @Provides
    fun provideValidateEmailUseCase(emailMatcher: EmailMatcher) = ValidateEmailUseCase(emailMatcher)

    @Provides
    fun provideValidateBirthdayUseCase(parseDateUseCase: ParseDateUseCase) =
        ValidateBirthdayUseCase(parseDateUseCase)

    @Provides
    fun provideParseDateUseCase() = ParseDateUseCase("yyyy-MM-dd")

    @Provides
    fun provideValidatePasswordUseCase() = ValidatePasswordUseCase()

    @Provides
    fun provideValidateConfirmPasswordUseCase() = ValidateConfirmPasswordUseCase()


    @Provides
    fun providePatternsEmailMatcher() = PatternsEmailMatcher()

    @Provides
    fun provideConvertLocalDateUseCase() = ConvertLocalDateUseCase()

}