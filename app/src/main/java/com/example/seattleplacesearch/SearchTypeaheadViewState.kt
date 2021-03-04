package com.example.seattleplacesearch

sealed class SearchTypeaheadViewState {
    class Default(val venueViewStates: List<VenueViewState>) : SearchTypeaheadViewState()
    class Error(val error: Throwable) : SearchTypeaheadViewState()
    object Empty : SearchTypeaheadViewState()
}