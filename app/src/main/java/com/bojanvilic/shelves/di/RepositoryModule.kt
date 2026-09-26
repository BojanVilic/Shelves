package com.bojanvilic.shelves.di

import com.bojanvilic.shelves.data.repository.DefaultOpenLibraryRepository
import com.bojanvilic.shelves.data.repository.OpenLibraryRepository
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
    abstract fun bindOpenLibraryRepository(
        impl: DefaultOpenLibraryRepository,
    ): OpenLibraryRepository
}
