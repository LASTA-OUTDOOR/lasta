package com.lastaoutdoor.lasta.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.utils.Response
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivitiesDBRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivityRepository
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainDispatcherRule(val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {
  @ExperimentalCoroutinesApi
  override fun starting(description: Description?) = Dispatchers.setMain(dispatcher)

  class DiscoveryScreenViewModelTest()

  override fun finished(description: Description?) = Dispatchers.resetMain()
}

class DiscoveryScreenViewModelTest() {
  @ExperimentalCoroutinesApi val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

  private var repo = FakeActivityRepository()

  private lateinit var viewModel: DiscoverScreenViewModel
  private lateinit var repository: ActivityRepository
  private lateinit var activitiesDB: FakeActivitiesDBRepository

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
  fun setUp() {
    repository = mockk(relaxed = true)
    activitiesDB = FakeActivitiesDBRepository()
    viewModel = DiscoverScreenViewModel(repository, activitiesDB)
    repo.currResponse = Response.Success(null)

    viewModel = DiscoverScreenViewModel(repo, mockk())
  }

  @ExperimentalCoroutinesApi
  @Test
  fun fetchBikingActivities() {
    runBlocking {
      viewModel.updateActivityType(ActivityType.BIKING)
      viewModel.fetchActivities()
      assertEquals(viewModel.activities.value, emptyList<Activity>())
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun fetchHikingActivities() {
    runBlocking {
      viewModel.updateActivityType(ActivityType.HIKING)
      viewModel.fetchActivities()
      assertEquals(viewModel.activities.value, emptyList<Activity>())
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun fetchClimbingActivities() {
    runBlocking {
      viewModel.updateActivityType(ActivityType.CLIMBING)
      viewModel.fetchActivities()
      assertEquals(viewModel.activities.value, emptyList<Activity>())
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun setScreen() {
    runBlocking {
      viewModel.setScreen(DiscoverDisplayType.LIST)
      assertEquals(viewModel.screen.value, DiscoverDisplayType.LIST)
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun setLoc() {
    runBlocking {
      viewModel.setSelectedLocality(Pair("a", LatLng(0.0, 0.0)))
      assertEquals(viewModel.selectedLocality.value, Pair("a", LatLng(0.0, 0.0)))
    }
  }

  @Test
  fun testInitialValues() {
    assertEquals(viewModel.activities.value.isEmpty(), true) // Check initial activities
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

  @ExperimentalCoroutinesApi
  @Test
  fun setRnage() {
    runBlocking {
      viewModel.setRange(0.0)
      assertEquals(viewModel.range.value, 0.0)
    }
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
    assertEquals(viewModel.markerList.value, emptyList<Marker>())
  }

  @Test
  fun update_selectedMarker() {
    viewModel.updateSelectedMarker(
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.BIKING))
    assertEquals(
        viewModel.selectedMarker.value,
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.BIKING))
    viewModel.clearSelectedMarker()
    // hiking
    viewModel.updateSelectedMarker(
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.HIKING))
    assertEquals(
        viewModel.selectedMarker.value,
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.HIKING))
    viewModel.clearSelectedMarker()
    // climbing
    viewModel.updateSelectedMarker(
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.CLIMBING))
    assertEquals(
        viewModel.selectedMarker.value,
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.CLIMBING))
  }
}
