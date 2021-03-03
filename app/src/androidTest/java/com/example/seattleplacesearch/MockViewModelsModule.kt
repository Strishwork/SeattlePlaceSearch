package com.example.seattleplacesearch

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.mockito.Mockito
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object MockViewModelsModule {

    @Provides
    @Singleton
    fun provideSearchViewModelFactory(): SearchViewModelFactory =
        Mockito.mock(SearchViewModelFactory::class.java)
}