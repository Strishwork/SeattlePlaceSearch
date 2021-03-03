package com.example.seattleplacesearch

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import com.example.seattleplacesearch.di.ViewModelsModule
import com.schibsted.spain.barista.assertion.BaristaListAssertions
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(ViewModelsModule::class)
class VenueDetailsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var searchViewModelFactoryMock: SearchViewModelFactory
    private val viewModelMock = Mockito.mock(SearchViewModel::class.java)
    private val venuesMutableLiveData = MutableLiveData<VenuePreviewViewState>()

    @Before
    fun init() {
        hiltRule.inject()
        Mockito.`when`(searchViewModelFactoryMock.create(SearchViewModel::class.java))
            .thenReturn(viewModelMock)
        Mockito.`when`(viewModelMock.venuesLiveData).thenReturn(venuesMutableLiveData)
        val scenario = launchFragmentInHiltContainer<VenueDetailsFragment>(bundleOf("venue" to generateVenue()))
    }


    @Test
    fun testVenueDetailFieldsAreVisibleAndCorrect() {
        assertDisplayed("Name 1")
        assertDisplayed("Category 1")
        assertDisplayed("Address 1")
        assertContains("50 m")
    }

    @Test
    fun testVenueDetailMapIsVisible() {
        assertDisplayed(R.id.detailsMap)
    }

    private fun generateVenue(): Venue {
        return Venue(
            "Name 1",
            "Category 1",
            VenueLocation(
                "Address 1",
                MapInfoHelper.SEATTLE_LATITUDE + 10,
                MapInfoHelper.SEATTLE_LONGITUDE - 10
            ),
            "",
            50
        )
    }
}