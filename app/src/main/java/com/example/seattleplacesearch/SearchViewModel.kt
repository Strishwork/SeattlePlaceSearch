package com.example.seattleplacesearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seattleplacesearch.api.VenuesApi
import com.example.seattleplacesearch.fragments.SearchFragment
import com.example.seattleplacesearch.testing.OpenForTesting
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@OpenForTesting
class SearchViewModel(private val venuesApi: VenuesApi) : ViewModel(),
    SearchFragment.SearchQueryUpdatedListener {

    private val venuesMutableLiveData = MutableLiveData<VenuePreviewViewState>()
    val venuesLiveData: LiveData<VenuePreviewViewState> = venuesMutableLiveData
    private lateinit var disposable: Disposable

    private fun getVenues(query: String) {
        if (query.trim().isEmpty()) {
            venuesMutableLiveData.postValue(VenuePreviewViewState.Empty)
            return
        }

        disposable = venuesApi.getVenuesList(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response -> onResponse(response) }, { t -> onFailure(t) })
    }

    private fun onFailure(t: Throwable) {
        venuesMutableLiveData.postValue(VenuePreviewViewState.Error(t))
    }

    private fun onResponse(response: JSONResponse<Response>) {
        when {
            response.response.venues.isEmpty() -> venuesMutableLiveData.postValue(
                VenuePreviewViewState.Empty
            )
            else -> {
                val venues = response.response.venues.map { venue ->
                    Venue(
                        name = venue.name ?: "-",
                        category = venue.categories.firstOrNull()?.name ?: "-",
                        location = VenueLocation(
                            address = venue.location.address ?: "Address unknown",
                            lat = venue.location.lat,
                            lng = venue.location.lng
                        ),
                        iconUrl = with(venue.categories.firstOrNull()?.icon) {
                            this?.prefix?.plus("bg_64")?.plus(this.suffix)
                        } ?: "",
                        distanceToCenter = MapInfoHelper.getDistanceToSeattleCenter(venue.location)
                    )
                }
                venuesMutableLiveData.postValue(VenuePreviewViewState.Default(venues))
            }
        }
    }

    override fun queryUpdated(query: String) {
        getVenues(query)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}