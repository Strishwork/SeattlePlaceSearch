package com.example.seattleplacesearch

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class JSONResponse (
    @SerializedName("response")
    val response: Response
)

data class Response(
    @SerializedName("venues")
    val venues: ArrayList<VenueDTO> = ArrayList()
)

data class VenueDTO(
    val name: String?,
    val categories: ArrayList<Category> = ArrayList(),
    val location: VenueLocation
)

data class Category(
    val name: String? = null,
    val icon: Icon
)

data class Icon(
    val prefix: String,
    val suffix: String
)

@Parcelize
data class VenueLocation(
    val address: String?,
    val lat: Double,
    val lng: Double
) : Parcelable


