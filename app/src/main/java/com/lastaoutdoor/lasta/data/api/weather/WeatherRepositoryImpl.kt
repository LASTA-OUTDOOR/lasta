package com.lastaoutdoor.lasta.data.api.weather

import com.lastaoutdoor.lasta.BuildConfig
import com.lastaoutdoor.lasta.repository.api.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val weatherApiService: WeatherApiService) :
    WeatherRepository {
  override suspend fun getCurrentWeather(city: String): WeatherResponse {
    return weatherApiService.getCurrentWeather(city, BuildConfig.WEATHER_API_KEY)
  }

  override suspend fun getWeatherWithLoc(lat: Double, lon: Double): WeatherResponse {
    return weatherApiService.getWeatherWithLoc(lat, lon, BuildConfig.WEATHER_API_KEY)
  }

  override suspend fun getForecastWeather(lat: Double, lon: Double): WeatherForecastResponse {
    return weatherApiService.getForecastWeather(lat, lon, BuildConfig.WEATHER_API_KEY)
  }
}
