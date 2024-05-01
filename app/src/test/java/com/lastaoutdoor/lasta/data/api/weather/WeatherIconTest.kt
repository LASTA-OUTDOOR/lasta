package com.lastaoutdoor.lasta.data.api.weather

import com.lastaoutdoor.lasta.R
import junit.framework.TestCase.assertEquals
import org.junit.Test

class WeatherIconTest {
  @Test
  fun test_getWeatherIcon() {
    assertEquals(R.drawable.weather_sun, getWeatherIconFromId("01d"))
    assertEquals(R.drawable.weather_fewclouds, getWeatherIconFromId("02d"))
    assertEquals(R.drawable.weather_clouds, getWeatherIconFromId("03d"))
    assertEquals(R.drawable.weather_brokenclouds, getWeatherIconFromId("04d"))
    assertEquals(R.drawable.weather_showerrain, getWeatherIconFromId("09d"))
    assertEquals(R.drawable.weather_rain, getWeatherIconFromId("10d"))
    assertEquals(R.drawable.weather_storm, getWeatherIconFromId("11d"))
    assertEquals(R.drawable.weather_snow, getWeatherIconFromId("13d"))
    assertEquals(R.drawable.weather_mist, getWeatherIconFromId("50d"))
    assertEquals(R.drawable.weather_moon, getWeatherIconFromId("01n"))
    assertEquals(R.drawable.weather_nightclouds, getWeatherIconFromId("02n"))
    assertEquals(R.drawable.weather_clouds, getWeatherIconFromId("03n"))
    assertEquals(R.drawable.weather_brokenclouds, getWeatherIconFromId("04n"))
    assertEquals(R.drawable.weather_showerrain, getWeatherIconFromId("09n"))
    assertEquals(R.drawable.weather_nightrain, getWeatherIconFromId("10n"))
    assertEquals(R.drawable.weather_storm, getWeatherIconFromId("11n"))
    assertEquals(R.drawable.weather_snow, getWeatherIconFromId("13n"))
    assertEquals(R.drawable.weather_mist, getWeatherIconFromId("50n"))
    assertEquals(R.drawable.weather_sun, getWeatherIconFromId("asdfgsdfgsd"))
  }
}
