package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.models.user.UserPreferences
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivityRepository
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class MapViewModelTest {
  @ExperimentalCoroutinesApi val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
  private val flow = flowOf(UserPreferences(true))
  val db = FakeActivityRepository()

  val vm = MapViewModel(db)

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

  @ExperimentalCoroutinesApi
  @Test
  fun MapViewModel() {
    vm.state
    vm.updatePermission(true)
    vm.updateSelectedMarker(mockk())
    vm._state
    vm.initialZoom
    vm.selectedZoom
    vm.clearSelectedItinerary()
    vm.updateSelectedItinerary(0)
    vm._state.value = MapState()
    vm.initialPosition
  }
}
