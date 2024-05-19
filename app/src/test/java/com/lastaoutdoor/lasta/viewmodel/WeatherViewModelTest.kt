package com.lastaoutdoor.lasta.viewmodel

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.viewmodel.repo.FakeWeatherRepository
import io.mockk.mockk
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
  private val errorToast = mockk<ErrorToast>()

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
    weatherViewModel = WeatherViewModel(weatherRepository, application, errorToast)
  }

  /*@Test
  fun testFetchWeather() {
      weatherViewModel = WeatherViewModel(weatherRepository, application)
      assert(weatherViewModel.weather.value != null)
  }*/
}
