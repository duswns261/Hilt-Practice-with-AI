package com.cret.hilt_practice.di

import com.cret.hilt_practice.data.repository.UserRepositoryImpl
import com.cret.hilt_practice.data.source.LocalUserDataSource
import com.cret.hilt_practice.data.source.UserDataSource
import com.cret.hilt_practice.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindUserDataSource(
        localUserDataSource: LocalUserDataSource
    ): UserDataSource
}
