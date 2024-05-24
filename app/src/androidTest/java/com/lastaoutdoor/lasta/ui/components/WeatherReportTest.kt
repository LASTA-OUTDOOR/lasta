package com.lastaoutdoor.lasta.ui.components

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import com.lastaoutdoor.lasta.data.api.weather.Main
import com.lastaoutdoor.lasta.data.api.weather.MainForecast
import com.lastaoutdoor.lasta.data.api.weather.Weather
import com.lastaoutdoor.lasta.data.api.weather.WeatherForecast
import com.lastaoutdoor.lasta.data.api.weather.WeatherForecastResponse
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.data.api.weather.Wind
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class WeatherReportTest {
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @Test
  fun whenNullWeather_isDisplayed() {
    composeRule.activity.setContent {
      val fakeWeatherResponse = null
      WeatherReportBig(weather = fakeWeatherResponse, displayWind = true) {}
    }
    composeRule.onNodeWithTag("NoLocMessage").assertIsDisplayed()
  }

  @Test
  fun weatherInfos_areDisplayed() {
    composeRule.activity.setContent {
      val fakeWeatherResponse =
          WeatherResponse("name", Main(3.0, 4.0), listOf(Weather("descr", "iconid")), Wind(5.0))
      WeatherReportBig(weather = fakeWeatherResponse, displayWind = true) {}
    }
    composeRule.onNodeWithTag("WeatherReportBig").assertIsDisplayed()
  }

  @Test
  fun windInfos_notDisplayed() {
    composeRule.activity.setContent {
      val fakeWeatherResponse =
          WeatherResponse("name", Main(3.0, 4.0), listOf(Weather("descr", "iconid")), Wind(5.0))
      WeatherReportBig(weather = fakeWeatherResponse, displayWind = false) {}
    }

    composeRule.onNodeWithTag("WindDisplay").assertIsNotDisplayed()
  }

  @Test
  fun smallForecast_isDisplayed() {
    composeRule.activity.setContent {
      val fakeWeatherResponse =
          WeatherResponse("name", Main(3.0, 4.0), listOf(Weather("descr", "iconid")), Wind(5.0))
      WeatherReportSmall(weather = fakeWeatherResponse) {}
    }
    composeRule.onNodeWithTag("temp").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Current Weather Logo").assertIsDisplayed()
  }

  @Test
  fun smallForecastWhenNull_isNotDisplayed() {
    composeRule.activity.setContent {
      val fakeWeatherResponse = null
      WeatherReportSmall(weather = fakeWeatherResponse) {}
    }

    composeRule.onNodeWithContentDescription("Current Weather Logo").assertIsNotDisplayed()
  }

  @Test
  fun weatherForecastDisplayTest() {
    composeRule.activity.setContent {
      val fakeWeatherForecastResponse =
          WeatherForecastResponse(
              listOf(
                  WeatherForecast(
                      MainForecast(2.0, 3.0, 2.0, 0.0), listOf(Weather("main", "icon")), "dt")))
      val weatherForecast = fakeWeatherForecastResponse.list.first()
      WeatherForecastDisplay(weatherForecast = weatherForecast, date = "")
    }
    composeRule.onNodeWithTag("forecast").assertIsDisplayed()
    composeRule.onNodeWithTag("forecastIcon").assertIsDisplayed()
  }

  @Test
  fun weatherForecastDisplayWhenNull_isNotDisplayed() {
    composeRule.activity.setContent {
      val fakeWeatherForecastResponse = null
      WeatherForecastDisplay(weatherForecast = fakeWeatherForecastResponse, date = "")
    }

    composeRule.onNodeWithTag("forecast").assertIsNotDisplayed()
    composeRule.onNodeWithTag("forecastIcon").assertIsNotDisplayed()
  }
}
