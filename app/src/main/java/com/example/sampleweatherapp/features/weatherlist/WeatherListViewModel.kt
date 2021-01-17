package com.example.sampleweatherapp.features.weatherlist

import androidx.lifecycle.*
import com.example.sampleweatherapp.CustomApplication
import com.example.sampleweatherapp.network.WeatherApi
import kotlinx.coroutines.launch
import retrofit2.await
import java.lang.Exception


enum class ApiStatus { LOADING, ERROR, DONE }

const val CITY_IDS = "1701668,3067696,1835848"


class WeatherListViewModel(private val weatherApi: WeatherApi) : ViewModel() {
    var apiErrorMessage = ""

    private val favoriteCityList = mutableSetOf<String>()
    private val _weatherList = MutableLiveData<List<WeatherModel>>()

    val weatherList: LiveData<List<WeatherModel>>
        get() = _weatherList

    private val _weatherDetail = MutableLiveData<WeatherModel>()

    val weatherDetail: LiveData<WeatherModel>
        get() = _weatherDetail

    private val _status = MutableLiveData<ApiStatus>()

    val status: LiveData<ApiStatus>
        get() = _status

    fun getWeatherListFromNetwork() {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            try {
                _weatherList.value =
                    weatherApi.getWeatherGroup(CITY_IDS, CustomApplication.appKey).await()
                        .toDomain(favoriteCityList)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                apiErrorMessage = e.message ?: "Error retrieving data"
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun getWeatherDetailFromNetwork(city: String) {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            try {
                val data = weatherApi.getWeatherData(city, CustomApplication.appKey).await()
                val domainData = data.toDomain(favoriteCityList)
                _weatherDetail.value = domainData
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                apiErrorMessage = e.message ?: "Error retrieving data"
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun toggleFavoriteCity(city: String) {
        val item = weatherDetail.value!!
        if (item.isFavorite) {
            changeFavor(city, false)
        } else {
            changeFavor(city, true)
        }
    }

    private fun changeFavor(city: String, isFavorite: Boolean) {
        val item = weatherDetail.value!!
        item.isFavorite = isFavorite
        _weatherDetail.value = item

        if (isFavorite) {
            favoriteCityList.add(city)
        } else {
            favoriteCityList.remove(city)
        }

    }
}