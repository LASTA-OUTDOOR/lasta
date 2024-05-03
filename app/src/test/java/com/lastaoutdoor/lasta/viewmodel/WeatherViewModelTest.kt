package com.lastaoutdoor.lasta.viewmodel

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.lastaoutdoor.lasta.viewmodel.repo.FakeWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.mockito.Mock

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

  @Mock private var weatherRepository = FakeWeatherRepository()
  private lateinit var application: Application
  private lateinit var weatherViewModel: WeatherViewModel

  @ExperimentalCoroutinesApi val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

  @ExperimentalCoroutinesApi
  @Before
  fun setupDispatcher() {
    Dispatchers.setMain(testDispatcher)
  }

  @ExperimentalCoroutinesApi
  @After
  fun tearDownDispatcher() {
    Dispatchers.resetMain()
    testDispatcher.cleanupTestCoroutines()
  }

  @Before
  fun setup() {
    application = ApplicationProvider.getApplicationContext()
    weatherViewModel = WeatherViewModel(weatherRepository, application)
  }

  /*@Test
  fun testFetchWeather() {
      weatherViewModel = WeatherViewModel(weatherRepository, application)
      assert(weatherViewModel.weather.value != null)
  }*/
}
