package com.example.seattleplacesearch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Venue(
    val name: String,
    val category: String,
    val location: VenueLocation,
    val iconUrl: String,
    val distanceToCenter: Int
) : Parcelable