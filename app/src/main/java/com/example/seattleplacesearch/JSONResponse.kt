package com.example.seattleplacesearch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class JSONResponse<T>(
    val response: T
)

data class Response(
    val venues: List<VenueDTO>
)

data class VenueDTO(
    val name: String,
    val categories: List<Category>,
    val location: VenueLocation
)

data class Category(
    val name: String,
    val icon: Icon
)

data class Icon(
    val prefix: String,
    val suffix: String
)

@Parcelize
data class VenueLocation(
    val address: String,
    val lat: Double,
    val lng: Double
) : Parcelable


