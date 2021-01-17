package com.example.sampleweatherapp.network

import com.example.sampleweatherapp.features.weatherlist.WeatherModel

data class GetWeatherDataResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
) {
    fun toDomain(favoriteCityList: MutableSet<String>): WeatherModel? {
        return WeatherModel(
            name,
            weather[0].main,
            String.format("%.1f", main.temp).plus(CELCIUS_SYMBOL),
            favoriteCityList.contains(name),
            "High ${String.format(
                "%.0f",
                main.temp_max
            )} $CELCIUS_SYMBOL / Low ${String.format("%.0f", main.temp_min)} $CELCIUS_SYMBOL"
        )
    }
}