package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.data.api.weather.Main
import com.lastaoutdoor.lasta.data.api.weather.MainForecast
import com.lastaoutdoor.lasta.data.api.weather.Weather
import com.lastaoutdoor.lasta.data.api.weather.WeatherForecast
import com.lastaoutdoor.lasta.data.api.weather.WeatherForecastResponse
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.data.api.weather.Wind
import com.lastaoutdoor.lasta.repository.api.WeatherRepository

class FakeWeatherRepository() : WeatherRepository {
  override suspend fun getCurrentWeather(city: String): WeatherResponse {
    return WeatherResponse(
        "weather", main = Main(2.0, 3.0), listOf(Weather("main", "icon")), Wind(1.0))
  }

  override suspend fun getWeatherWithLoc(lat: Double, lon: Double): WeatherResponse {
    return WeatherResponse(
        "weather", main = Main(2.0, 3.0), listOf(Weather("main", "icon")), Wind(1.0))
  }

  override suspend fun getForecastWeather(lat: Double, lon: Double): WeatherForecastResponse {
    return WeatherForecastResponse(
        listOf(
            WeatherForecast(
                MainForecast(2.0, 3.0, 2.0, 0.0), listOf(Weather("main", "icon")), "dt")))
  }
}
