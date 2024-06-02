package com.lastaoutdoor.lasta.viewmodel

import android.hardware.Sensor
import android.hardware.SensorManager
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrackingViewModelTest {

  private val sensorManager: SensorManager = mockk(relaxed = true)
  private val sensor: Sensor = mockk(relaxed = true)
  private lateinit var viewModel: TrackingViewModel

  private var stepCount = 0

  private val testDispatcher = UnconfinedTestDispatcher()

  @Before
  fun setUp() {
    try {
      Dispatchers.setMain(testDispatcher)
      every { sensorManager.getDefaultSensor(any()) } returns sensor
      viewModel = TrackingViewModel(sensorManager)
    } catch (exception: Exception) {
      exception.printStackTrace()
    }
  }

  @After
  fun tearDown() {
    try {
      clearAllMocks()
      Dispatchers.resetMain()
    } catch (exception: Exception) {
      exception.printStackTrace()
    }
  }

  @Test
  fun `state is correct`() {
    assert(viewModel.state.value.sensorManager == sensorManager)
    assert(viewModel.state.value.sensor == sensor)
    assert(viewModel.state.value.stepCount == stepCount)
    assert(viewModel.state.value.positions.isEmpty())
    assert(viewModel.state.value.distanceDone == 0.0)
  }

  /*@Test
  fun `updateStepCount updates step count correctly`() = runTest {
    viewModel.updateStepCount(100)
    assertEquals(stepCount, viewModel.state.value.stepCount)

    stepCount = 10
    viewModel.updateStepCount(110)
    assertEquals(stepCount, viewModel.state.value.stepCount)
  }

  @Test
  fun `locationCallback updates positions and distances correctly`() = runTest {
    Dispatchers.resetMain()
    val mockLocationResult: LocationResult = mockk(relaxed = true)
    val mockLocation1 =
        mockk<android.location.Location>(relaxed = true) {
          every { latitude } returns 10.0
          every { longitude } returns 20.0
        }
    val mockLocation2 =
        mockk<android.location.Location>(relaxed = true) {
          every { latitude } returns 10.0
          every { longitude } returns 20.1
        }
    every { mockLocationResult.lastLocation } returnsMany listOf(mockLocation1, mockLocation2)

    val locationCallback = viewModel.getLocationCallBack()

    locationCallback.onLocationResult(mockLocationResult)
    assertEquals(1, viewModel.state.value.positions.size)
    assertEquals(Position(10.0, 20.0), viewModel.state.value.positions.first())
    assertTrue(viewModel.state.value.distances.isEmpty())

    locationCallback.onLocationResult(mockLocationResult)
    assertEquals(2, viewModel.state.value.positions.size)
    assertEquals(Position(10.0, 20.1), viewModel.state.value.positions.last())
    assertEquals(1, viewModel.state.value.distances.size)
    assert(viewModel.state.value.distances.isNotEmpty())
  }*/
}
