package com.example.seattleplacesearch

sealed class VenuePreviewViewState {
    class Default(val venues: List<Venue>) : VenuePreviewViewState()
    class Error(val error: Throwable) : VenuePreviewViewState()
    object Empty : VenuePreviewViewState()
}