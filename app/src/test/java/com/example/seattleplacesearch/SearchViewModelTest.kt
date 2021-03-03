package com.example.seattleplacesearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.seattleplacesearch.api.VenuesApi
import io.mockk.every
import io.mockk.mockkObject
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.Answers
import org.mockito.ArgumentCaptor
import org.mockito.Mockito


class SearchViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private val venuesApiMock = Mockito.mock(VenuesApi::class.java)

    private val viewModel by lazy {
        SearchViewModel(venuesApiMock)
    }

    @Test
    fun testEmptyState() {
        val mockObserver = Mockito.mock(Observer::class.java) as Observer<VenuePreviewViewState>
        viewModel.venuesLiveData.observeForever(mockObserver)
        (viewModel as SearchFragment.SearchQueryUpdatedListener).queryUpdated("")
        val argumentCaptor = ArgumentCaptor.forClass(VenuePreviewViewState::class.java)
        Mockito.verify(mockObserver, Mockito.times(1)).onChanged(argumentCaptor.capture())

        assert(argumentCaptor.allValues.last() is VenuePreviewViewState.Empty)
    }

    @Test
    fun testVenuePreviewDefaultViewState() {
        val mockResponse: JSONResponse<Response> =
            Mockito.mock(JSONResponse::class.java, Answers.RETURNS_DEEP_STUBS) as JSONResponse<Response>
        val mockData: Response =
            Mockito.mock(Response::class.java, Answers.RETURNS_DEEP_STUBS) as Response
        val data = mockVenues()

        mockkObject(MapInfoHelper)

        every { MapInfoHelper.getDistanceToSeattleCenter(any()) } returns 100

        Mockito.`when`(mockResponse.response).thenReturn(mockData)
        Mockito.`when`(mockData.venues).thenReturn(data)
        Mockito.`when`(venuesApiMock.getVenuesList("test"))
            .thenReturn(Observable.just(mockResponse))

        val mockObserver = Mockito.mock(Observer::class.java) as Observer<VenuePreviewViewState>
        viewModel.venuesLiveData.observeForever(mockObserver)

        (viewModel as SearchFragment.SearchQueryUpdatedListener).queryUpdated("test")

        val argumentCaptor = ArgumentCaptor.forClass(VenuePreviewViewState::class.java)
        Mockito.verify(mockObserver, Mockito.times(1)).onChanged(argumentCaptor.capture())

        assert(argumentCaptor.allValues.last() is VenuePreviewViewState.Default)

        val actualState = argumentCaptor.allValues.last() as VenuePreviewViewState.Default

        Assert.assertThat(actualState.venues[0].name, `is`("Test name"))
        Assert.assertThat(actualState.venues[0].category, `is`("Test category"))
        Assert.assertThat(actualState.venues[0].location.address, `is`("Test address"))
    }

    @Test
    fun testVenuePreviewErrorViewState() {
        Mockito.`when`(venuesApiMock.getVenuesList("test"))
            .thenReturn(Observable.error(Throwable("Test error")))

        val mockObserver = Mockito.mock(Observer::class.java) as Observer<VenuePreviewViewState>
        viewModel.venuesLiveData.observeForever(mockObserver)

        (viewModel as SearchFragment.SearchQueryUpdatedListener).queryUpdated("test")

        val argumentCaptor = ArgumentCaptor.forClass(VenuePreviewViewState::class.java)
        Mockito.verify(mockObserver, Mockito.times(1)).onChanged(argumentCaptor.capture())

        assert(argumentCaptor.allValues.last() is VenuePreviewViewState.Error)

        val actualState = argumentCaptor.allValues.last() as VenuePreviewViewState.Error

        Assert.assertThat(actualState.error.message, `is`("Test error"))
    }

    private fun mockVenues(): List<VenueDTO> {
        val mockVenuesDto =
            Mockito.mock(VenueDTO::class.java, Answers.RETURNS_DEEP_STUBS)
        mockVenuesDto.apply {
            Mockito.`when`(mockVenuesDto.name).thenReturn("Test name")
            Mockito.`when`(mockVenuesDto.categories)
                .thenReturn(listOf(Category("Test category", Icon("", ""))))
            Mockito.`when`(mockVenuesDto.location)
                .thenReturn(VenueLocation("Test address", 1.1, 2.2))
        }
        return listOf(mockVenuesDto)
    }
}

class RxImmediateSchedulerRule : TestRule {

    override fun apply(base: Statement, d: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}