package com.lastaoutdoor.lasta.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.ClimbingStyle
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.utils.OrderingBy
import com.lastaoutdoor.lasta.utils.Response
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivitiesDBRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivityRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeRadarRepository
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
  private lateinit var prefRepo: FakePreferencesRepository
  private lateinit var radarRepo: FakeRadarRepository

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
    prefRepo = FakePreferencesRepository()
    radarRepo = FakeRadarRepository()
    activitiesDB = FakeActivitiesDBRepository()
    viewModel = DiscoverScreenViewModel(repository, prefRepo, activitiesDB, radarRepo)
    repo.currResponse = Response.Success(null)
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
    assertEquals(viewModel.suggestions.value, emptyMap<String, LatLng>())
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
    assertEquals(viewModel.mapState.value.uiSettings.myLocationButtonEnabled, true)
    assertEquals(viewModel.mapState.value.properties.isMyLocationEnabled, true)
    viewModel.updatePermission(false)
    assertEquals(viewModel.mapState.value.properties.isMyLocationEnabled, false)
    assertEquals(viewModel.mapState.value.uiSettings.myLocationButtonEnabled, false)
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
  fun filterWithDiff_worksAsIntended() {
    var res = viewModel.filterWithDiff(ActivityType.BIKING, UserLevel.BEGINNER, Activity("", 0L))
    assertEquals(false, res)
    res = viewModel.filterWithDiff(ActivityType.CLIMBING, UserLevel.BEGINNER, Activity("", 0L))
    assertEquals(true, res)
    res =
        viewModel.filterWithDiff(
            ActivityType.BIKING,
            UserLevel.INTERMEDIATE,
            Activity("", 0L, activityType = ActivityType.BIKING))
    assertEquals(false, res)
    res =
        viewModel.filterWithDiff(
            ActivityType.HIKING,
            UserLevel.ADVANCED,
            Activity("", 0L, activityType = ActivityType.HIKING))
    assertEquals(false, res)
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

  @Test
  fun activitiesToMarkers_worksProperly() {
    val activities =
        listOf(
            Activity(
                "id",
                1,
                ActivityType.BIKING,
                "description",
                Position(0.0, 0.0),
                1f,
                5,
                emptyList(),
                Difficulty.EASY,
                "url",
                ClimbingStyle.OUTDOOR,
                1f,
                "from",
                "to",
                1f))
    val markers = viewModel.activitiesToMarkers(activities)
    assertEquals(markers.size, 1)
    assertEquals(markers[0].id, 1)
    assertEquals(markers[0].name, "description")
    assertEquals(markers[0].position, LatLng(0.0, 0.0))
    assertEquals(markers[0].description, "")
  }

  @Test
  fun testUpdateOrderingBy_WithEmptyActivities() {
    viewModel.updateOrderingBy(OrderingBy.RATING)
    assertEquals(viewModel.orderingBy.value, OrderingBy.RATING)
    viewModel.updateOrderingBy(OrderingBy.DISTANCE)
    assertEquals(viewModel.orderingBy.value, OrderingBy.DISTANCE)
    viewModel.updateOrderingBy(OrderingBy.DIFFICULTYASCENDING)
    assertEquals(viewModel.orderingBy.value, OrderingBy.DIFFICULTYASCENDING)
    viewModel.updateOrderingBy(OrderingBy.DIFFICULTYDESCENDING)
    assertEquals(viewModel.orderingBy.value, OrderingBy.DIFFICULTYDESCENDING)
    viewModel.updateOrderingBy(OrderingBy.POPULARITY)
    assertEquals(viewModel.orderingBy.value, OrderingBy.POPULARITY)
  }

  @Test
  fun testUpdateOrderingBy_WithActivities() {
    viewModel.updateActivities(
        listOf(
            Activity(
                "id",
                1,
                ActivityType.BIKING,
                "description",
                Position(0.0, 0.0),
                1f,
                5,
                emptyList(),
                Difficulty.EASY,
                "url",
                ClimbingStyle.OUTDOOR,
                1f,
                "from",
                "to",
                1f)))

    viewModel.updateOrderingBy(OrderingBy.RATING)
    assertEquals(viewModel.orderingBy.value, OrderingBy.RATING)
    viewModel.updateOrderingBy(OrderingBy.DISTANCE)
    assertEquals(viewModel.orderingBy.value, OrderingBy.DISTANCE)
    viewModel.updateOrderingBy(OrderingBy.DIFFICULTYASCENDING)
    assertEquals(viewModel.orderingBy.value, OrderingBy.DIFFICULTYASCENDING)
    viewModel.updateOrderingBy(OrderingBy.DIFFICULTYDESCENDING)
    assertEquals(viewModel.orderingBy.value, OrderingBy.DIFFICULTYDESCENDING)
    viewModel.updateOrderingBy(OrderingBy.POPULARITY)
    assertEquals(viewModel.orderingBy.value, OrderingBy.POPULARITY)
  }

  // Test autocompletion part of the view model
  @Test
  fun testFetchSuggestionsAndClear() {
    viewModel.fetchSuggestions("Test")
    assert(viewModel.suggestions.value.isNotEmpty())
    assert(viewModel.suggestions.value.size == 4)

    viewModel.clearSuggestions()
    assert(viewModel.suggestions.value.isEmpty())
  }

  @Test
  fun testActivitiesToMarkers() {
    val testActivity =
        Activity(
            "id",
            1,
            ActivityType.BIKING,
            "description",
            Position(0.0, 0.0),
            1f,
            5,
            emptyList(),
            Difficulty.EASY,
            "url",
            ClimbingStyle.OUTDOOR,
            1f,
            "from",
            "to",
            1f)
    val marker = viewModel.activitiesToMarkers(listOf(testActivity))
    assert(marker.isNotEmpty())
    assert(marker.size == 1)
    assert(marker.first().position == LatLng(0.0, 0.0))
  }

  @Test
  fun randomSmallChecks() {
    viewModel.setSelectedActivityType(ActivityType.BIKING)
    assert(viewModel.selectedActivityType.value == ActivityType.BIKING)

    assert(viewModel.selectedZoom == 13f)
    assert(viewModel.initialPosition.value == LatLng(46.519962, 6.633597))
    assert(viewModel.initialZoom == 11f)
  }
}
