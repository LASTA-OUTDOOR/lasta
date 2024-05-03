package com.lastaoutdoor.lasta.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.api.Relation
import com.lastaoutdoor.lasta.models.api.Tags
import com.lastaoutdoor.lasta.models.map.MapItinerary
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.utils.Response
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class DiscoveryScreenViewModelTest {

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: DiscoverScreenViewModel
  private lateinit var repository: ActivityRepository

  @Before
  fun setUp() {
    repository = mockk(relaxed = true)
    viewModel = DiscoverScreenViewModel(repository)
  }

  @Test
  fun testInitialValues() {
    assertEquals(viewModel.activities.value.isEmpty(), false) // Check initial activities
    assertEquals(viewModel.screen.value, DiscoverDisplayType.LIST)
    assertEquals(viewModel.range.value, 10000.0)
    assertEquals(
        viewModel.localities,
        listOf(
            "Ecublens" to LatLng(46.519962, 6.633597),
            "Geneva" to LatLng(46.2043907, 6.1431577),
            "Payerne" to LatLng(46.834190, 6.928969),
            "Matterhorn" to LatLng(45.980537, 7.641618)))
  }

  @Test
  fun testPUpdatePermissions() {
    viewModel.updatePermission(true)
    assertEquals(viewModel.initialPosition, LatLng(46.519962, 6.633597))
    viewModel.updatePermission(false)
    assertEquals(viewModel.initialPosition, LatLng(46.519962, 6.633597))
  }

  @Test
  fun testClearSelectedItinerary() {
    viewModel.clearSelectedItinerary()
    assertEquals(viewModel.selectedItinerary.value, null)
  }

  @Test
  fun testClearSelectedMarker() {
    viewModel.clearSelectedMarker()
    assertEquals(viewModel.selectedMarker.value, null)
  }

  @Test
  fun testUpdateMarkers() {
    viewModel.updateMarkers(LatLng(46.519962, 6.633597), 10000.0)
    assertEquals(viewModel.markerList.value, viewModel.markerList.value)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `updateSelectedMarker updates marker and triggers itinerary fetch`() = runTest {
    withContext(Dispatchers.Main) {

      // Prepare
      val marker =
          Marker(1L, "Trail Name", LatLng(45.0, -45.0), "Description", 0, ActivityType.HIKING)
      val expectedRelation = Relation("Relationt Test", 1L, Tags(""), emptyList())
      val expectedItinerary = MapItinerary(1L, "Trail Name", listOf(LatLng(45.0, -45.0)))

      // Mock the repository response
      coEvery { repository.getHikingRouteById(1L) } returns Response.Success(expectedRelation)

      //
      viewModel.updateSelectedMarker(marker)
      // Wait for the coroutine to finish
      advanceUntilIdle()
      assertEquals(marker, viewModel.selectedMarker.value)
    }
  }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()) :
    TestWatcher() {
  override fun starting(description: Description) {
    Dispatchers.setMain(testDispatcher)
  }

  override fun finished(description: Description) {
    Dispatchers.resetMain()
  }
}
