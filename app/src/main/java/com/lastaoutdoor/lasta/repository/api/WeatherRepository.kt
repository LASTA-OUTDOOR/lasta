package com.lastaoutdoor.lasta.repository.api

import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse

interface WeatherRepository {
  suspend fun getCurrentWeather(city: String): WeatherResponse

  suspend fun getWeatherWithLoc(lat: Double, lon: Double): WeatherResponse
}
