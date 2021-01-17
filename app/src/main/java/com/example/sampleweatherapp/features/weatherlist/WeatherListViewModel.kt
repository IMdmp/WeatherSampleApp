package com.example.sampleweatherapp.features.weatherlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sampleweatherapp.CustomApplication
import com.example.sampleweatherapp.network.WeatherApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.await


enum class ApiStatus {LOADING,ERROR,DONE}
const val CITY_IDS ="524901,703448,2643743"


class WeatherListViewModel(private val weatherApi: WeatherApi):ViewModel() {
    private val _status = MutableLiveData<ApiStatus>()

    val status: LiveData<ApiStatus>
        get() = _status

    private val _weatherList = MutableLiveData<List<WeatherModel>>()

    val weatherList:LiveData<List<WeatherModel>>
        get() = _weatherList

    fun getDataFromNetwork() {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            try{
                _weatherList.value =  weatherApi.getWeatherGroup(CITY_IDS,CustomApplication.appKey).await().toDomain()
                _status.value = ApiStatus.DONE
            }catch (e: HttpException){
                _status.value = ApiStatus.ERROR
            }
        }
    }

}