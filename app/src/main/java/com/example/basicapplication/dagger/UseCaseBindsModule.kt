package com.example.basicapplication.dagger

import com.example.basicapplication.util.EmailMatcher
import com.example.basicapplication.util.PatternsEmailMatcher
import dagger.Binds
import dagger.Module

@Module
interface UseCaseBindsModule {
    @Binds
    fun bindEmailMatcher(patternsEmailMatcher: PatternsEmailMatcher): EmailMatcher
}