package com.example.seattleplacesearch.di

import com.example.seattleplacesearch.api.VenuesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RetrofitModule {

    private const val BASE_URL = "https://api.foursquare.com/v2/venues/"

    @Provides
    @Singleton
    fun provideRetrofit(): VenuesApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VenuesApi::class.java)
    }


}