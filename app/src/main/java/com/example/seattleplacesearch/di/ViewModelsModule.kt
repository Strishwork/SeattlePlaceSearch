package com.example.seattleplacesearch.di

import com.example.seattleplacesearch.SearchViewModelFactory
import com.example.seattleplacesearch.api.VenuesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object ViewModelsModule {

    @Provides
    @Singleton
    fun provideSearchViewModelFactory(retrofitClient: VenuesApi): SearchViewModelFactory {
        return SearchViewModelFactory(retrofitClient)
    }
}