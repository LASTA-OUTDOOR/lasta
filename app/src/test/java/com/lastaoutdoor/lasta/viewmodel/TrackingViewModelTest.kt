package com.lastaoutdoor.lasta.viewmodel

import android.hardware.Sensor
import android.hardware.SensorManager
import com.google.android.gms.location.LocationResult
import com.lastaoutdoor.lasta.models.api.Position
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TrackingViewModelTest {

  private val sensorManager: SensorManager = mockk(relaxed = true)
  private val sensor: Sensor = mockk(relaxed = true)
  private lateinit var viewModel: TrackingViewModel

  @Before
  fun setUp() {
    every { sensorManager.getDefaultSensor(any()) } returns sensor
    viewModel = TrackingViewModel(sensorManager)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `state is correct`() {
    assert(viewModel.state.value.sensorManager == sensorManager)
    assert(viewModel.state.value.sensor == sensor)
    assert(viewModel.state.value.stepCount == 0)
    assert(viewModel.state.value.positions.isEmpty())
    assert(viewModel.state.value.distances.isEmpty())
  }

  @Test
  fun `updateStepCount updates step count correctly`() = runTest {
    viewModel.updateStepCount(100)
    assertEquals(0, viewModel.state.value.stepCount)

    viewModel.updateStepCount(110)
    assertEquals(10, viewModel.state.value.stepCount)
  }

  @Test
  fun `locationCallback updates positions and distances correctly`() = runTest {
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

    viewModel.locationCallback.onLocationResult(mockLocationResult)
    assertEquals(1, viewModel.state.value.positions.size)
    assertEquals(Position(10.0, 20.0), viewModel.state.value.positions.first())
    assertTrue(viewModel.state.value.distances.isEmpty())

    viewModel.locationCallback.onLocationResult(mockLocationResult)
    assertEquals(2, viewModel.state.value.positions.size)
    assertEquals(Position(10.0, 20.1), viewModel.state.value.positions.last())
    assertEquals(1, viewModel.state.value.distances.size)
    assert(viewModel.state.value.distances.isNotEmpty())
  }
}
