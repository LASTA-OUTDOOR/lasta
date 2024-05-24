package com.lastaoutdoor.lasta.viewmodel

import android.app.Application
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.viewmodel.repo.FakeWeatherRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

  @Mock private var weatherRepository = FakeWeatherRepository()
  private lateinit var application: Application
  private lateinit var weatherViewModel: WeatherViewModel
  private val errorToast = mockk<ErrorToast>()

  @ExperimentalCoroutinesApi val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

  @ExperimentalCoroutinesApi
  @Before
  fun setupDispatcher() {
    Dispatchers.setMain(testDispatcher)
    every { errorToast.showToast(any()) } returns Unit
  }

  @ExperimentalCoroutinesApi
  @After
  fun tearDownDispatcher() {
    Dispatchers.resetMain()
    testDispatcher.cleanupTestCoroutines()
  }

  @Before
  fun setup() {
    application = Application()
    weatherViewModel = WeatherViewModel(weatherRepository, application, errorToast)
  }

  @Test
  fun testFetchWeather() {
    assert(weatherViewModel.weather.value == null)
  }

  // get weather forecast works
  @Test
  fun testFetchWeatherForecastNull() {
    weatherViewModel.getForecast()
    assert(weatherViewModel.weatherForecast.value == null)
  }

  @Test
  fun fetchWeatherWithUserLoc() {
    weatherViewModel.fetchWeatherWithUserLoc()
    assert(weatherViewModel.weather.value == null)
  }

  // Change location of weather works (without wifi services so just functioning call)
  @Test
  fun changeLocOfWeather() {
    weatherViewModel.changeLocOfWeather(
        Activity("a", 10, activityType = ActivityType.CLIMBING, difficulty = Difficulty.EASY))
    assert(weatherViewModel.weather.value == null)
  }
}
