package com.example.sampleweatherapp.network

import com.example.sampleweatherapp.features.weatherlist.WeatherModel

data class GetWeatherGroupResponse(
    val cnt: Int,
    val list: List<WeatherData>
) {
    fun toDomain(favoriteCityList: MutableSet<String>): List<WeatherModel> {
        val retList = mutableListOf<WeatherModel>()
        list.forEach {
            retList.add(it.toDomain(favoriteCityList.contains(it.name)))
        }
        return retList
    }
}

data class WeatherData(
    val clouds: Clouds,
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
    fun toDomain(isFavorite: Boolean): WeatherModel {
        return WeatherModel(
            locationName = name,
            weatherStatus = weather[0].main,
            currentTemp = String.format("%.1f",main.temp),
            isFavorite = isFavorite,
            lowAndHighTemperature = ""
        )
    }
}

data class Clouds(
    val all: Int
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class Main(
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Sys(
    val country: String,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

data class Wind(
    val deg: Int,
    val speed: Float
)