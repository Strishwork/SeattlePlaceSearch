package com.example.seattleplacesearch.api

import com.example.seattleplacesearch.JSONResponse
import com.example.seattleplacesearch.MapInfoHelper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VenuesApi {
    companion object{
        private const val CLIENT_ID = "PLYLKOV3EQL0SWQF3PTAEFT1G5FWFGPATDCZKUE4S1VXPWTG"
        private const val CLIENT_SECRET = "XNHOGJITQO3SSX3L03T04UMPG5ENMBALCBGCGI52CRIJ3BHI"
        private const val API_VERSION = "20180323"
    }

    @GET("search?v=$API_VERSION")
    fun getVenuesList(
        @Query("query") query: String,
        @Query("client_id") id: String = CLIENT_ID,
        @Query("client_secret") secret: String = CLIENT_SECRET,
        @Query("near") location: String = MapInfoHelper.SEATTLE
    ): Call<JSONResponse>
}