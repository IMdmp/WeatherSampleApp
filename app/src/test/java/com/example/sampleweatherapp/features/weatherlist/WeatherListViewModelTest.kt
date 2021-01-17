package com.example.sampleweatherapp.features.weatherlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sampleweatherapp.CoroutineTestRule
import com.example.sampleweatherapp.CustomApplication
import com.example.sampleweatherapp.features.getOrAwaitValue
import com.example.sampleweatherapp.network.*
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.mock.Calls


@ExperimentalCoroutinesApi
class WeatherListViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel:WeatherListViewModel

    @Mock
    private lateinit var weatherApi: WeatherApi

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val mockWeatherData = WeatherData(
        Clouds(100),
        Coord(-0.1257,51.5085),
        1610840244,
        1,
        Main(35.4,
            66,
            1022,
            27.5,
            29.2,
            25.2),
        "Manila",
        Sys("PH",
            1610922298,
            1610963247,
            8160),
        10000,
        listOf(Weather("overcast clouds","04d",804,"main")),
        Wind(1,2.9f)
    )
    private val mockSample:GetWeatherGroupResponse = GetWeatherGroupResponse(
        1,
        listOf(mockWeatherData)
    )


    private val mockGetWeatherData = GetWeatherDataResponse(
        "stations",
        Clouds(100),
        200,
        Coord(-0.1257,51.5085),
        1610840244,
        1,
        Main(35.4,
            66,
            1022,
            27.5,
            29.2,
            25.2),
        "Manila",
        Sys("PH",
            1610922298,
            1610963247,
            8160),
        10000,
        listOf(Weather("overcast clouds","04d",804,"main")),
        Wind(1,2.9f)
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel= WeatherListViewModel(weatherApi)
    }

    @Test
    fun loadWeatherListFromNetwork_withSuccess() {
        `when`(weatherApi.getWeatherGroup(CITY_IDS,CustomApplication.appKey)).thenReturn(Calls.response(mockSample))
        viewModel.getWeatherListFromNetwork()

        val result = viewModel.weatherList.getOrAwaitValue()
        Assert.assertNotNull(result)
    }

    @Test
    fun loadWeatherListFromNetwork_triggersStatus(){
        coroutineTestRule.pauseDispatcher()

        `when`(weatherApi.getWeatherGroup(CITY_IDS,CustomApplication.appKey)).thenReturn(Calls.response(mockSample))
        viewModel.getWeatherListFromNetwork()


        assertThat(viewModel.status.getOrAwaitValue(),`is`(ApiStatus.LOADING))

        coroutineTestRule.resumeDispatcher()

        assertThat(viewModel.status.getOrAwaitValue(),`is`(ApiStatus.DONE))
    }

    @Test
    fun loadGetWeatherDetail_withSuccess() {
        `when`(weatherApi.getWeatherData("Manila",CustomApplication.appKey)).thenReturn(Calls.response(mockGetWeatherData))
        viewModel.getWeatherDetailFromNetwork("Manila")

        val result = viewModel.weatherDetail.getOrAwaitValue()
        Assert.assertNotNull(result)
    }

    @Test
    fun favoriteCitySetsFavoriteInDetail(){
        `when`(weatherApi.getWeatherData("Manila",CustomApplication.appKey)).thenReturn(Calls.response(mockGetWeatherData))
        `when`(weatherApi.getWeatherGroup(CITY_IDS,CustomApplication.appKey)).thenReturn(Calls.response(mockSample))

        viewModel.getWeatherDetailFromNetwork("Manila")

        viewModel.toggleFavoriteCity("Manila")
        viewModel.getWeatherListFromNetwork()

        val result = viewModel.weatherList.getOrAwaitValue()

        Assert.assertNotNull(result.find {it.locationName == "Manila"})
        val favoriteResult = result.find {it.locationName == "Manila"}!!.isFavorite

        assertTrue(favoriteResult)
    }
}