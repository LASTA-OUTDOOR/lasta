package com.lastaoutdoor.lasta.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.ClimbingStyle
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.models.api.Relation
import com.lastaoutdoor.lasta.models.api.SimpleWay
import com.lastaoutdoor.lasta.models.api.Tags
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
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

  override fun finished(description: Description?) = Dispatchers.resetMain()
}

class DiscoveryScreenViewModelTest() {
  @ExperimentalCoroutinesApi val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

  private var repo = FakeActivityRepository()

  private lateinit var viewModel: DiscoverScreenViewModel
  private lateinit var repository: FakeActivityRepository
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
    repository = FakeActivityRepository()
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
      viewModel.updateActivityType(listOf(ActivityType.BIKING))

      assertEquals(viewModel.selectedActivityType.value, listOf(ActivityType.BIKING))

      assertEquals(
          viewModel.activities.value,
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
      viewModel.updateActivityType(listOf(ActivityType.HIKING))

      assertEquals(viewModel.selectedActivityType.value, listOf(ActivityType.HIKING))

      assertEquals(
          viewModel.activities.value,
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
      viewModel.updateActivityType(listOf(ActivityType.CLIMBING))

      assertEquals(viewModel.selectedActivityType.value, listOf(ActivityType.CLIMBING))

      assertEquals(
          viewModel.activities.value,
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
  fun setRange() {
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
    // biking, id = 0
    viewModel.updateSelectedMarker(
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.BIKING))
    assertEquals(
        viewModel.selectedMarker.value,
        Marker(0, "name", LatLng(0.0, 0.0), "description", 1, ActivityType.BIKING))
    viewModel.clearSelectedMarker()

    // biking, id != 0
    viewModel.updateSelectedMarker(
        Marker(
            1,
            "name",
            LatLng(0.0, 0.0),
            "description",
            R.drawable.biking_icon,
            ActivityType.BIKING))
    assertEquals(
        viewModel.selectedMarker.value,
        Marker(
            1,
            "name",
            LatLng(0.0, 0.0),
            "description",
            R.drawable.biking_icon,
            ActivityType.BIKING))
    viewModel.clearSelectedMarker()

    // hiking, id != 0
    viewModel.updateSelectedMarker(
        Marker(
            1,
            "name",
            LatLng(0.0, 0.0),
            "description",
            R.drawable.hiking_icon,
            ActivityType.HIKING))
    assertEquals(
        viewModel.selectedMarker.value,
        Marker(
            1,
            "name",
            LatLng(0.0, 0.0),
            "description",
            R.drawable.hiking_icon,
            ActivityType.HIKING))
    viewModel.clearSelectedMarker()

    // climbing
    viewModel.updateSelectedMarker(
        Marker(
            2,
            "name",
            LatLng(0.0, 0.0),
            "description",
            R.drawable.climbing_icon,
            ActivityType.CLIMBING))
    assertEquals(
        viewModel.selectedMarker.value,
        Marker(
            2,
            "name",
            LatLng(0.0, 0.0),
            "description",
            R.drawable.climbing_icon,
            ActivityType.CLIMBING))

    // marker icon null
    viewModel.updateSelectedMarker(
        Marker(2, "name", LatLng(0.0, 0.0), "description", -1, ActivityType.CLIMBING))
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
    val testActivityBiking =
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
    val markerBiking = viewModel.activitiesToMarkers(listOf(testActivityBiking))
    assert(markerBiking.isNotEmpty())
    assert(markerBiking.size == 1)
    assert(markerBiking.first().position == LatLng(0.0, 0.0))

    val testActivityHiking = testActivityBiking.copy(activityType = ActivityType.HIKING)
    val markerHiking = viewModel.activitiesToMarkers(listOf(testActivityHiking))
    assert(markerHiking.isNotEmpty())
    assert(markerHiking.size == 1)
    assert(markerHiking.first().position == LatLng(0.0, 0.0))

    val testActivityClimbing = testActivityBiking.copy(activityType = ActivityType.CLIMBING)
    val markerClimbing = viewModel.activitiesToMarkers(listOf(testActivityClimbing))
    assert(markerClimbing.isNotEmpty())
    assert(markerClimbing.size == 1)
    assert(markerClimbing.first().position == LatLng(0.0, 0.0))
  }

  @Test
  fun randomSmallChecks() {
    viewModel.setSelectedActivitiesType(listOf(ActivityType.BIKING))
    assert(viewModel.selectedActivityType.value.contains(ActivityType.BIKING))

    assert(viewModel.selectedZoom == 16f)
    assert(viewModel.initialPosition.value == LatLng(46.519962, 6.633597))
    assert(viewModel.initialZoom == 11f)
  }

  @Test
  fun testShowItinerary() {
    val id = 0L
    val startPosition = LatLng(0.0, 0.0)
    var activityType = ActivityType.HIKING

    val method =
        viewModel.javaClass.getDeclaredMethod(
            "showItinerary", Long::class.java, LatLng::class.java, ActivityType::class.java)
    method.isAccessible = true

    repository.currResponse = Response.Success(null)

    method.invoke(viewModel, id, startPosition, activityType)

    assert(viewModel.selectedItinerary.value != null)

    activityType = ActivityType.BIKING
    method.invoke(viewModel, id, startPosition, activityType)
    assert(viewModel.selectedItinerary.value != null)

    activityType = ActivityType.CLIMBING
    method.invoke(viewModel, id, startPosition, activityType)
    assert(viewModel.selectedItinerary.value != null)
  }

  @Suppress("UNCHECKED_CAST")
  @Test
  fun testShowRouteItinerary() {

    val ways = listOf(SimpleWay(listOf(Position(0.0, 0.0), Position(15.0, 15.0))))

    val relation: Relation = Relation("test", 1L, Tags(), ways as List<SimpleWay>?)

    val method =
        viewModel.javaClass.getDeclaredMethod(
            "showRouteItinerary", Long::class.java, Relation::class.java, LatLng::class.java)
    method.isAccessible = true

    method.invoke(viewModel, 0L, relation, LatLng(14.0, 14.0))

    assert(viewModel.selectedItinerary.value != null)
  }
}
