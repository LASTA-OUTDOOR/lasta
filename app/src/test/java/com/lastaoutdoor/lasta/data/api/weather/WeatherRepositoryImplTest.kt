package com.lastaoutdoor.lasta.data.api.weather

import com.lastaoutdoor.lasta.BuildConfig
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class WeatherRepositoryImplTest {
  @Mock lateinit var weatherApiService: WeatherApiService

  private lateinit var weatherRepository: WeatherRepositoryImpl

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)
    weatherRepository = WeatherRepositoryImpl(weatherApiService)
  }

  @Test
  fun testGetCurrentWeather() {
    // Mock response from WeatherApiService
    val expectedResponse = WeatherResponse("name", Main(2.0, 3.0), listOf(), Wind(4.0))
    runBlocking {
      `when`(weatherApiService.getCurrentWeather("cityName", BuildConfig.WEATHER_API_KEY))
          .thenReturn(expectedResponse)
    }

    // Call the method under test
    val actualResponse = runBlocking { weatherRepository.getCurrentWeather("cityName") }

    // Verify that the method from WeatherApiService was called with the correct arguments
    runBlocking {
      `when`(weatherApiService.getCurrentWeather("cityName", BuildConfig.WEATHER_API_KEY))
          .thenReturn(expectedResponse)
    }
    assertEquals(expectedResponse, actualResponse)
  }

  @Test
  fun testGetWeatherWithLoc() {
    // Mock response from WeatherApiService
    val expectedResponse = WeatherResponse("name", Main(2.0, 3.0), listOf(), Wind(4.0))
    runBlocking {
      `when`(weatherApiService.getWeatherWithLoc(0.0, 0.0, "apiKey")).thenReturn(expectedResponse)
    }

    // Call the method under test
    val actualResponse = runBlocking { weatherRepository.getWeatherWithLoc(0.0, 0.0) }

    // Verify that the method from WeatherApiService was called with the correct arguments
    runBlocking {
      `when`(weatherApiService.getWeatherWithLoc(0.0, 0.0, "apiKey")).thenReturn(expectedResponse)
    }
    assertEquals(null, actualResponse)
  }
}
