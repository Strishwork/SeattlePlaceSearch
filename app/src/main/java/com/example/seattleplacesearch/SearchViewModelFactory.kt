package com.example.seattleplacesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seattleplacesearch.interfaces.RetrofitServices
import javax.inject.Inject

class SearchViewModelFactory @Inject constructor(
    private val retrofitClient: RetrofitServices
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(retrofitClient) as T
    }

}