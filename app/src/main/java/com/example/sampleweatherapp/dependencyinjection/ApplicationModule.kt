package com.example.sampleweatherapp.dependencyinjection

import android.app.Application
import com.example.sampleweatherapp.CustomApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private var mApplication: CustomApplication) {

    @Provides
    @Singleton
    internal fun providesApplication(): Application {
        return mApplication
    }

}