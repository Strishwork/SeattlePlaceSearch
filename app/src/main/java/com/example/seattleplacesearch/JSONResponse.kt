package com.example.seattleplacesearch

import com.google.gson.annotations.SerializedName

data class JSONResponse (
    @SerializedName("response")
    var response: Resp
)

data class Resp(
    @SerializedName("venues")
    var venues: ArrayList<Venue> = ArrayList()
)

data class Venue(
    var name: String? = null,
    var categories: ArrayList<Category> = ArrayList(),
    var location: Location
)

data class Category(
    var name: String? = null
)

data class Location(
    var lat: Float,
    var lng: Float
)


