package com.example.seattleplacesearch

import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.seattleplacesearch.di.ViewModelsModule
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickBack
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(ViewModelsModule::class)
@RunWith(AndroidJUnit4ClassRunner::class)
class FragmentNavigationTest {

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
        setVenue()
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun test_navigationToDetailFragmentAndBack() {
        clickOn(R.id.typeaheadCardLayout)
        assertDisplayed(R.id.detailsName)
        clickBack()
        assertDisplayed(R.id.typeaheadCardLayout)
    }

    private fun setVenue() {
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
            )
        )
        venuesMutableLiveData.postValue(SearchTypeaheadViewState.Default(venues))
    }

}