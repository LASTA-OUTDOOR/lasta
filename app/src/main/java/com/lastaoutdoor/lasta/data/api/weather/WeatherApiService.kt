package com.lastaoutdoor.lasta.data.api.weather

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
  @GET("weather")
  suspend fun getCurrentWeather(
      @Query("q") city: String,
      @Query("appid") apiKey: String
  ): WeatherResponse

  @GET("weather")
  suspend fun getWeatherWithLoc(
      @Query("lat") lat: Double,
      @Query("lon") lon: Double,
      @Query("appid") apiKey: String
  ): WeatherResponse

  @GET("forecast")
  suspend fun getForecastWeather(
      @Query("lat") lat: Double,
      @Query("lon") lon: Double,
      @Query("appid") apiKey: String
  ): WeatherForecastResponse
}
