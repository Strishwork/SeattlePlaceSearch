package com.example.seattleplacesearch.api

import com.example.seattleplacesearch.JSONResponse
import com.example.seattleplacesearch.MapInfoHelper
import com.example.seattleplacesearch.Response
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VenuesApi {
    companion object{
        private const val API_VERSION = "20180323"
    }

    @GET("search?v=$API_VERSION")
    fun getVenuesList(
        @Query("query") query: String,
        @Query("near") location: String = MapInfoHelper.SEATTLE
    ): Observable<JSONResponse<Response>>
}