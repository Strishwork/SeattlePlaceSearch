package com.example.seattleplacesearch.di

import com.example.seattleplacesearch.BuildConfig
import com.example.seattleplacesearch.api.VenuesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RetrofitModule {

    private const val BASE_URL = "https://api.foursquare.com/v2/venues/"

    class TokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var original = chain.request()
            val url = original.url().newBuilder()
                .addQueryParameter("client_id", BuildConfig.API_CLIENT_ID)
                .addQueryParameter("client_secret", BuildConfig.API_CLIENT_SECRET)
                .build()
            original = original.newBuilder().url(url).build()
            return chain.proceed(original)
        }
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(): VenuesApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(VenuesApi::class.java)
    }


}