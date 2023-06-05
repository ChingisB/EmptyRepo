package com.example.basicapplication.dagger

import dagger.Module

@Module(includes = [RepositoryBindsModule::class])
interface RepositoryModule {
}