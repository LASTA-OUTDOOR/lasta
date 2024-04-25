package com.lastaoutdoor.lasta.data.api

import com.lastaoutdoor.lasta.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): WeatherResponse
}

object WeatherApi {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(WeatherApiService::class.java)

    suspend fun getCurrentWeather(city: String): WeatherResponse {
        return service.getCurrentWeather(city, BuildConfig.WEATHER_API_KEY)
    }
}