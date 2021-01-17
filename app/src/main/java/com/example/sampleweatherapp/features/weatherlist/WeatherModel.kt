package com.example.sampleweatherapp.features.weatherlist

data class WeatherModel(
    var locationName: String,
    var weatherStatus: String,
    var currentTemp: String,
    var isFavorite: Boolean,
    var lowAndHighTemperature:String=""
) {
}