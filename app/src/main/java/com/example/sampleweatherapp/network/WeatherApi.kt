package com.example.sampleweatherapp.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
const val CELCIUS_SYMBOL = " \u2103"

interface WeatherApi {

    @GET("group?units=metric")
    fun getWeatherGroup(@Query("id") id:String,@Query("appid") appId:String): Call<GetWeatherGroupResponse>

    @GET("weather?units=metric")
    fun getWeatherData(@Query("q") city:String,@Query("appid") appId:String):Call<GetWeatherDataResponse>

    companion object{

        operator fun invoke(retrofit: Retrofit):WeatherApi{
            return retrofit.create(WeatherApi::class.java)
        }
    }
}