package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.data.api.WeatherResponse

interface WeatherRepository {
    suspend fun getCurrentWeather(city: String): WeatherResponse
}