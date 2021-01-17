package com.example.sampleweatherapp

import android.app.Application
import com.example.sampleweatherapp.dependencyinjection.ApplicationComponent
import com.example.sampleweatherapp.dependencyinjection.ApplicationModule
import com.example.sampleweatherapp.dependencyinjection.DaggerApplicationComponent
import com.example.sampleweatherapp.dependencyinjection.NetworkModule
import timber.log.Timber

class CustomApplication:Application() {
    companion object{
        const val appKey ="873046276e65f35590a72961b7234ea5"

    }
    lateinit var applicationComponent: ApplicationComponent
    
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this@CustomApplication))
            .networkModule(NetworkModule())
            .build()
    }
}