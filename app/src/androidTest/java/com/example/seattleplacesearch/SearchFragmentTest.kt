package com.example.seattleplacesearch

import androidx.lifecycle.MutableLiveData
import com.example.seattleplacesearch.di.ViewModelsModule
import com.example.seattleplacesearch.fragments.SearchFragment
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(ViewModelsModule::class)
class SearchFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var searchViewModelFactoryMock: SearchViewModelFactory
    private val viewModelMock = Mockito.mock(SearchViewModel::class.java)
    private val venuesMutableLiveData = MutableLiveData<SearchTypeaheadViewState>()

    @Before
    fun init() {
        hiltRule.inject()
        Mockito.`when`(searchViewModelFactoryMock.create(SearchViewModel::class.java))
            .thenReturn(viewModelMock)
        Mockito.`when`(viewModelMock.venuesLiveData).thenReturn(venuesMutableLiveData)
        val scenario = launchFragmentInHiltContainer<SearchFragment>()
    }



    @Test
    fun testSearchEditTextAndButtonAreVisible() {
        assertDisplayed(R.id.searchEditText)
        assertDisplayed(R.id.startSearchButton)
    }

    @Test
    fun testSearchButtonAreInvisibleAfterClicked() {
        clickOn(R.id.startSearchButton)
        assertNotDisplayed(R.id.startSearchButton)
    }

    @Test
    fun testVenuesAreVisibleAndCorrect() {
        setVenues()
        assertRecyclerViewItemCount(R.id.searchTypeaheadRecycler, 2)

        assertDisplayedAtPosition(R.id.searchTypeaheadRecycler, 0, R.id.venueName, "Name 1")
        assertDisplayedAtPosition(R.id.searchTypeaheadRecycler, 0, R.id.venueCategory, "Category 1")
        assertDisplayedAtPosition(R.id.searchTypeaheadRecycler, 0, R.id.venueDistance, "50 m")

        assertDisplayedAtPosition(R.id.searchTypeaheadRecycler, 1, R.id.venueName, "Name 2")
        assertDisplayedAtPosition(R.id.searchTypeaheadRecycler, 1, R.id.venueCategory, "Category 2")
        assertDisplayedAtPosition(R.id.searchTypeaheadRecycler, 1, R.id.venueDistance, "100 m")
    }


    private fun setVenues() {
        val venues = listOf(
            VenueViewState(
                "Name 1",
                "Category 1",
                VenueLocation(
                    "Address 1",
                    1.1,
                    2.2
                    ),
                "",
                50
            ),
            VenueViewState(
                "Name 2",
                "Category 2",
                VenueLocation(
                    "Address 2",
                    1.1,
                    2.2
                ),
                "",
                100
            )
        )
        venuesMutableLiveData.postValue(SearchTypeaheadViewState.Default(venues))
    }
}