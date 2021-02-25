package com.example.seattleplacesearch.common

import com.example.seattleplacesearch.`interface`.RetrofitServices
import com.example.seattleplacesearch.retrofit.RetrofitClient

object Common {
    private const val BASE_URL = "https://api.foursquare.com/v2/venues/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}