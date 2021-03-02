package com.example.seattleplacesearch

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import kotlin.math.roundToInt


object MapInfoHelper {

    const val SEATTLE = "Seattle, WA"
    const val SEATTLE_LATITUDE = 47.6062
    const val SEATTLE_LONGITUDE = -122.3321

    fun getDistanceToSeattleCenter(location: VenueLocation): Int{
        val a = FloatArray(1)
        Location.distanceBetween(SEATTLE_LATITUDE, SEATTLE_LONGITUDE, location.lat, location.lng, a)
        return a.first().roundToInt()
    }
}