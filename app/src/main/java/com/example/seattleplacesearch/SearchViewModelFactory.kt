package com.example.seattleplacesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seattleplacesearch.api.VenuesApi
import com.example.seattleplacesearch.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class SearchViewModelFactory @Inject constructor(
    private val retrofitClient: VenuesApi
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(retrofitClient) as T
    }

}