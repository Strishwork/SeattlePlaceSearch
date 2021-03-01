package com.example.seattleplacesearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seattleplacesearch.interfaces.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val retrofitClient: RetrofitServices) : ViewModel(),
    SearchFragment.SearchQueryUpdatedListener {

    private val venuesMutableLiveData = MutableLiveData<VenuePreviewViewState>()
    val venuesLiveData: LiveData<VenuePreviewViewState> = venuesMutableLiveData

    private fun getVenues(query: String) {
        if (query.trim().isEmpty()) {
            venuesMutableLiveData.postValue(VenuePreviewViewState.Empty)
            return
        }
        retrofitClient.getVenuesList(query).enqueue(object : Callback<JSONResponse> {
            override fun onFailure(call: Call<JSONResponse>, t: Throwable) {
                venuesMutableLiveData.postValue(VenuePreviewViewState.Error(t))
            }

            override fun onResponse(
                call: Call<JSONResponse>,
                response: Response<JSONResponse>
            ) {
                val response = response.body()?.response?.venues
                if (response == null) {
                    venuesMutableLiveData.postValue(VenuePreviewViewState.Error(Throwable("Venues are not available")))
                } else {
                    val venues = response.map { venue ->
                        Venue(
                            name = venue.name ?: "-",
                            category = venue.categories.firstOrNull()?.name ?: "-",
                            location = venue.location,
                            iconUrl = with(venue.categories.firstOrNull()?.icon) {
                                this?.prefix?.plus("bg_64")?.plus(this.suffix)
                            } ?: ""
                        )
                    }
                    venuesMutableLiveData.postValue(VenuePreviewViewState.Default(venues))
                }
            }
        })
    }

    override fun queryUpdated(query: String) {
        getVenues(query)
    }
}