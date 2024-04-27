package com.lastaoutdoor.lasta.data.api.weather

import com.lastaoutdoor.lasta.BuildConfig
import com.lastaoutdoor.lasta.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val weatherApiService: WeatherApiService) :
    WeatherRepository {
  override suspend fun getCurrentWeather(city: String): WeatherResponse {
    return weatherApiService.getCurrentWeather(city, BuildConfig.WEATHER_API_KEY)
  }
}