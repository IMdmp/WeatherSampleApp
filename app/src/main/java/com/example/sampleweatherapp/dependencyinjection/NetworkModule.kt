package com.example.sampleweatherapp.dependencyinjection

import com.example.sampleweatherapp.BuildConfig
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
class NetworkModule {

    @Singleton
    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1

        val client = OkHttpClient.Builder()

        // Add logging only for debug mode
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }

        return client.build()
    }

    @Singleton
    @Provides
    internal fun provideGson():Gson{
        return Gson().newBuilder().create()
    }

    @Singleton
    @Provides
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.SERVER_URL)
            .client(okHttpClient)
            .build()
    }


}