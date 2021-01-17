package com.example.sampleweatherapp.dependencyinjection

import com.example.sampleweatherapp.features.weatherdetail.WeatherDetailFragment
import com.example.sampleweatherapp.features.weatherlist.WeatherListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApplicationModule::class,
        NetworkModule::class]
)
interface ApplicationComponent {
    fun inject(weatherListFragment: WeatherListFragment)
    fun inject(weatherDetailFragment: WeatherDetailFragment)
}