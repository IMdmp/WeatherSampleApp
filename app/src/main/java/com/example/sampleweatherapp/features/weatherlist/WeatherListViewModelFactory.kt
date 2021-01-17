package com.example.sampleweatherapp.features.weatherlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sampleweatherapp.network.WeatherApi
import javax.inject.Inject

class WeatherListViewModelFactory @Inject constructor(private val weatherApi: WeatherApi): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherListViewModel::class.java)) {
            return WeatherListViewModel(weatherApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}