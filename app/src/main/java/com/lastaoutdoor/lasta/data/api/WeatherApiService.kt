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

