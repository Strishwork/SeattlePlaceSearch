package com.example.seattleplacesearch

import java.io.Serializable

data class Venue(
    val name: String,
    val category: String,
    val location: VenueLocation,
    val iconUrl: String
) : Serializable