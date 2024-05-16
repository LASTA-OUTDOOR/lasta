package com.lastaoutdoor.lasta.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.ClimbingStyle
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.utils.OrderingBy
import com.lastaoutdoor.lasta.utils.Response
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivitiesDBRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivityRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeRadarRepository
import io.mockk.coEvery
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
    viewModel = DiscoverScreenViewModel(repository, prefRepo, activitiesDB, radarRepo, mockk())
    repo.currResponse = Response.Success(null)
  }

  @ExperimentalCoroutinesApi
  @Test
  fun fetchBikingActivities() {
    runBlocking {
      val fakeRel = FakeActivityRepository().fakeRel
      coEvery { repository.getBikingRoutesInfo(any(), any(), any()) } returns
          Response.Success(listOf(fakeRel))

      viewModel.updateActivityType(listOf(ActivityType.BIKING))

      assertEquals(viewModel.state.value.selectedActivityTypes, listOf(ActivityType.BIKING))

      assertEquals(
          viewModel.state.value.activities,
          listOf(
              Activity(
                  "id",
                  1,
                  ActivityType.BIKING,
                  "activityImageUrl",
                  Position(0.0, 0.0),
                  2f,
                  3,
              )))
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun fetchHikingActivities() {
    runBlocking {
      val fakeRel = FakeActivityRepository().fakeRel
      coEvery { repository.getHikingRoutesInfo(any(), any(), any()) } returns
          Response.Success(listOf(fakeRel))

      viewModel.updateActivityType(listOf(ActivityType.HIKING))

      assertEquals(viewModel.state.value.selectedActivityTypes, listOf(ActivityType.HIKING))

      assertEquals(
          viewModel.state.value.activities,
          listOf(
              Activity(
                  "id",
                  1,
                  ActivityType.BIKING,
                  "activityImageUrl",
                  Position(0.0, 0.0),
                  2f,
                  3,
              )))
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun fetchClimbingActivities() {
    runBlocking {
      val fakeNode = FakeActivityRepository().fakeNode
      coEvery { repository.getClimbingPointsInfo(any(), any(), any()) } returns
          Response.Success(listOf(fakeNode))

      viewModel.updateActivityType(listOf(ActivityType.CLIMBING))

      assertEquals(viewModel.state.value.selectedActivityTypes, listOf(ActivityType.CLIMBING))

      assertEquals(
          viewModel.state.value.activities,
          listOf(
              Activity(
                  "id",
                  1,
                  ActivityType.BIKING,
                  "activityImageUrl",
                  Position(0.0, 0.0),
                  2f,
                  3,
              )))
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun setScreen() {
    runBlocking {
      viewModel.setScreen(DiscoverDisplayType.LIST)
      assertEquals(viewModel.state.value.screen, DiscoverDisplayType.LIST)
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun setLoc() {
    runBlocking {
      viewModel.setSelectedLocality(Pair("a", LatLng(0.0, 0.0)))
      assertEquals(viewModel.state.value.selectedLocality, Pair("a", LatLng(0.0, 0.0)))
    }
  }

  @Test
  fun testInitialValues() {
    assertEquals(viewModel.state.value.activities.isEmpty(), true) // Check initial activities
    assertEquals(viewModel.state.value.screen, DiscoverDisplayType.LIST)
    assertEquals(viewModel.state.value.range, 10000.0)
    assertEquals(viewModel.state.value.suggestions, emptyMap<String, LatLng>())
    assertEquals(
        viewModel.state.value.localities,
        listOf(
            "Ecublens" to LatLng(46.519962, 6.633597),
            "Geneva" to LatLng(46.2043907, 6.1431577),
            "Payerne" to LatLng(46.834190, 6.928969),
            "Matterhorn" to LatLng(45.980537, 7.641618)))
  }

  @Test
  fun testPUpdatePermissions() {
    viewModel.updatePermission(true)
    assertEquals(viewModel.state.value.mapState.uiSettings.myLocationButtonEnabled, true)
    assertEquals(viewModel.state.value.mapState.properties.isMyLocationEnabled, true)
    viewModel.updatePermission(false)
    assertEquals(viewModel.state.value.mapState.properties.isMyLocationEnabled, false)
    assertEquals(viewModel.state.value.mapState.uiSettings.myLocationButtonEnabled, false)
  }

  @ExperimentalCoroutinesApi
  @Test
  fun setRange() {
    runBlocking {
      viewModel.setRange(0.0)
      assertEquals(viewModel.state.value.range, 0.0)
    }
  }

  @Test
  fun testClearSelectedItinerary() {
    viewModel.clearSelectedItinerary()
    assertEquals(viewModel.state.value.selectedItinerary, null)
  }

  @Test
  fun testClearSelectedMarker() {
    viewModel.clearSelectedMarker()
    assertEquals(viewModel.state.value.selectedMarker, null)
  }

  @Test
  fun testUpdateMarkers() {
    viewModel.updateMarkers(LatLng(46.519962, 6.633597), 10000.0)
    assertEquals(viewModel.state.value.markerList, emptyList<Marker>())
  }

  @Test
  fun filterWithDiff_worksAsIntended() {
    val userActivitiesLevel =
        UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.ADVANCED, UserLevel.INTERMEDIATE)

    var res = viewModel.filterWithDiff(userActivitiesLevel, Activity("", 0L))
    assertEquals(true, res)

    res =
        viewModel.filterWithDiff(
            userActivitiesLevel,
            Activity("", 0L, activityType = ActivityType.BIKING, difficulty = Difficulty.HARD))
    assertEquals(false, res)

    res =
        viewModel.filterWithDiff(
            userActivitiesLevel,
            Activity("", 0L, activityType = ActivityType.BIKING, difficulty = Difficulty.NORMAL))
    assertEquals(true, res)

    res =
        viewModel.filterWithDiff(
            userActivitiesLevel,
            Activity("", 0L, activityType = ActivityType.HIKING, difficulty = Difficulty.HARD))
    assertEquals(true, res)
  }

  @Test
  fun update_selectedMarker() {
    viewModel.updateSelectedMarker(
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.BIKING))
    assertEquals(
        viewModel.state.value.selectedMarker,
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.BIKING))
    viewModel.clearSelectedMarker()
    // hiking
    viewModel.updateSelectedMarker(
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.HIKING))
    assertEquals(
        viewModel.state.value.selectedMarker,
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.HIKING))
    viewModel.clearSelectedMarker()
    // climbing
    viewModel.updateSelectedMarker(
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.CLIMBING))
    assertEquals(
        viewModel.state.value.selectedMarker,
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
    assertEquals(viewModel.state.value.orderingBy, OrderingBy.RATING)
    viewModel.updateOrderingBy(OrderingBy.DISTANCE)
    assertEquals(viewModel.state.value.orderingBy, OrderingBy.DISTANCE)
    viewModel.updateOrderingBy(OrderingBy.DIFFICULTYASCENDING)
    assertEquals(viewModel.state.value.orderingBy, OrderingBy.DIFFICULTYASCENDING)
    viewModel.updateOrderingBy(OrderingBy.DIFFICULTYDESCENDING)
    assertEquals(viewModel.state.value.orderingBy, OrderingBy.DIFFICULTYDESCENDING)
    viewModel.updateOrderingBy(OrderingBy.POPULARITY)
    assertEquals(viewModel.state.value.orderingBy, OrderingBy.POPULARITY)
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
    assertEquals(viewModel.state.value.orderingBy, OrderingBy.RATING)
    viewModel.updateOrderingBy(OrderingBy.DISTANCE)
    assertEquals(viewModel.state.value.orderingBy, OrderingBy.DISTANCE)
    viewModel.updateOrderingBy(OrderingBy.DIFFICULTYASCENDING)
    assertEquals(viewModel.state.value.orderingBy, OrderingBy.DIFFICULTYASCENDING)
    viewModel.updateOrderingBy(OrderingBy.DIFFICULTYDESCENDING)
    assertEquals(viewModel.state.value.orderingBy, OrderingBy.DIFFICULTYDESCENDING)
    viewModel.updateOrderingBy(OrderingBy.POPULARITY)
    assertEquals(viewModel.state.value.orderingBy, OrderingBy.POPULARITY)
  }

  // Test autocompletion part of the view model
  @Test
  fun testFetchSuggestionsAndClear() {
    viewModel.fetchSuggestions("Test")
    assert(viewModel.state.value.suggestions.isNotEmpty())
    assert(viewModel.state.value.suggestions.size == 4)

    viewModel.clearSuggestions()
    assert(viewModel.state.value.suggestions.isEmpty())
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
    viewModel.setSelectedActivitiesType(listOf(ActivityType.BIKING))
    assert(viewModel.state.value.selectedActivityTypes.contains(ActivityType.BIKING))

    assert(viewModel.state.value.selectedZoom == 13f)
    assert(viewModel.state.value.initialPosition == LatLng(46.519962, 6.633597))
    assert(viewModel.state.value.initialZoom == 11f)
  }
}
