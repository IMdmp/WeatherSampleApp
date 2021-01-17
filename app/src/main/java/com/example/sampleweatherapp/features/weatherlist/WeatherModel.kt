package com.example.sampleweatherapp.features.weatherlist

data class WeatherModel(
    val locationName: String,
    val weatherStatus: String,
    val currentTemp: String,
    val isFavorite: Boolean
) {
}