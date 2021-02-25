package com.example.seattleplacesearch.`interface`

import com.example.seattleplacesearch.JSONResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServices {
    companion object{
        private const val CLIENT_ID = "PLYLKOV3EQL0SWQF3PTAEFT1G5FWFGPATDCZKUE4S1VXPWTG"
        private const val CLIENT_SECRET = "XNHOGJITQO3SSX3L03T04UMPG5ENMBALCBGCGI52CRIJ3BHI"
        private const val API_VERSION = "20180323"
    }

    @GET("search?client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET&v=$API_VERSION&near=Seattle,+WA")
    fun getVenuesList(@Query("query") query: String): Call<JSONResponse>
}