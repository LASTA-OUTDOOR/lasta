package com.lastaoutdoor.lasta.ui.screen.discover

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.ui.screen.discover.components.RangeSearchComposable
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.OrderingBy
import com.lastaoutdoor.lasta.viewmodel.DiscoverDisplayType
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenCallBacks
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class DiscoverScreenTest {
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  private lateinit var discoverScreenState: DiscoverScreenState
  private lateinit var discoverScreenCallBacks: DiscoverScreenCallBacks

  @Before
  fun setUp() {
    hiltRule.inject()

    discoverScreenState =
        DiscoverScreenState(
            activities =
                ArrayList(
                    listOf(
                        Activity(
                            "1",
                            11033919,
                            ActivityType.HIKING,
                            "Chemin panorama alpin",
                            Position(46.4718332, 6.8338907),
                            0.0f,
                            0,
                            emptyList(),
                            difficulty = Difficulty.EASY,
                            activityImageUrl = ""))))

    discoverScreenCallBacks =
        DiscoverScreenCallBacks(
            fetchActivities = {},
            setScreen = {},
            setSelectedLocality = {},
            updatePermission = {},
            updateSelectedMarker = {},
            clearSelectedItinerary = {},
            updateOrderingBy = {},
            clearSelectedMarker = {},
            fetchSuggestion = {},
            clearSuggestions = {},
            updateInitialPosition = {},
            updateActivities = {},
            updateRange = {},
            setSelectedActivitiesType = {},
            setSelectedLevels = {},
            setShowCompleted = {})
  }

  @Test
  fun headerComposable() {
    var navigateFilter = false
    var navigateSearch = false
    var screenType = DiscoverDisplayType.LIST

    val fetchSuggestion: (String) -> Unit = { /*TODO*/}
    var suggestion: Map<String, LatLng> = emptyMap()

    composeRule.activity.setContent {
      MaterialTheme {
        // Assuming you have a minimal setup or mock data needed for DiscoverScreen
        HeaderComposable(
            screen = screenType,
            range = 10000.0,
            selectedLocality = "Ecublens" to LatLng(46.519962, 6.633597),
            fetchActivities = {},
            setScreen = { screenType = it },
            navigateToRangeSearch = { navigateSearch = true },
            navigateToFilter = { navigateFilter = true },
            orderingBy = OrderingBy.RATING,
            updateOrderingBy = {},
            weather = null,
            fetchSuggestion = fetchSuggestion,
            suggestions = suggestion,
            setSelectedLocality = {},
            clearSuggestions = { suggestion = emptyMap() },
            updateInitialPosition = {},
            moveCamera = { _ -> },
            ConnectionState.CONNECTED)
      }
    }

    composeRule.onNodeWithTag("header").assertIsDisplayed()
    composeRule.onNodeWithTag("locationBar").assertIsDisplayed()
    composeRule.onNodeWithTag("locationRow").assertIsDisplayed()
    composeRule.onNodeWithTag("searchBarComponent").assertIsDisplayed()
    composeRule.onNodeWithTag("locationButton").performClick()
    composeRule.onNodeWithTag("filterButton").performClick()
    composeRule.waitForIdle()
    assertTrue(navigateFilter)
    assertTrue(navigateSearch)
    assertEquals(screenType, DiscoverDisplayType.LIST)
    composeRule.onNodeWithTag("sortingText").assertIsDisplayed()
    composeRule.onNodeWithTag("spinnerIcon").assertIsDisplayed()
    composeRule.onNodeWithTag("spinnerIcon").performClick()
  }

  @Test
  fun loading_displayed() {
    var screenType = DiscoverDisplayType.LIST
    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            discoverScreenState = discoverScreenState,
            discoverScreenCallBacks = discoverScreenCallBacks,
            favorites = emptyList(),
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            navigateToRangeSearch = { /*TODO*/},
            changeActivityToDisplay = {},
            changeWeatherTarget = {},
            weather = null,
            ConnectionState.CONNECTED)
      }
    }
    composeRule.onNodeWithTag("LoadingBarDiscover").assertIsDisplayed()
  }

  @Test
  fun activitiesDisplay() {
    composeRule.activity.setContent {
      MaterialTheme {
        // Assuming you have a minimal setup or mock data needed for DiscoverScreen
        ActivitiesDisplay(
            activities =
                listOf(
                    Activity(
                        "0",
                        11033919,
                        ActivityType.BIKING,
                        "Chemin panorama alpin",
                        Position(46.4718332, 6.8338907),
                        0.0f,
                        0,
                        emptyList(),
                        difficulty = Difficulty.EASY,
                        activityImageUrl = ""),
                    Activity(
                        "1",
                        11033919,
                        ActivityType.HIKING,
                        "Chemin panorama alpin",
                        Position(46.4718332, 6.8338907),
                        0.0f,
                        0,
                        emptyList(),
                        difficulty = Difficulty.EASY,
                        activityImageUrl = ""),
                    Activity(
                        "2",
                        11033919,
                        ActivityType.CLIMBING,
                        "Chemin panorama alpin",
                        Position(46.4718332, 6.8338907),
                        0.0f,
                        0,
                        emptyList(),
                        difficulty = Difficulty.EASY,
                        activityImageUrl = "")),
            centerPoint = LatLng(46.519962, 6.633597),
            favorites = emptyList(),
            changeActivityToDisplay = {},
            flipFavorite = {},
            navigateToMoreInfo = {},
            changeWeatherTarget = {},
            isOnline = ConnectionState.CONNECTED)
      }
    }

    composeRule.onNodeWithTag("0activityCard").assertIsDisplayed()
    composeRule.onNodeWithTag("1activityCard").assertIsDisplayed()
    composeRule.onNodeWithTag("2activityCard").assertIsDisplayed()
    composeRule.onNodeWithTag("0activityCard").performClick()
    composeRule.onNodeWithTag("1activityCard").performClick()
    composeRule.onNodeWithTag("2activityCard").performClick()
    composeRule.onNodeWithTag("0favoriteButton").performClick()
  }

  @Test
  fun discoverScreenList() {

    var state = discoverScreenState.copy(isLoading = false, screen = DiscoverDisplayType.LIST)

    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            discoverScreenState = state,
            discoverScreenCallBacks =
                discoverScreenCallBacks.copy(
                    setScreen = { _ -> state = state.copy(screen = DiscoverDisplayType.MAP) }),
            favorites = emptyList(),
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            navigateToRangeSearch = { /*TODO*/},
            changeActivityToDisplay = {},
            changeWeatherTarget = {},
            weather = null,
            ConnectionState.CONNECTED)
      }
    }

    composeRule.onNodeWithTag("discoveryScreen").assertIsDisplayed()
    composeRule.onNodeWithTag("SelectionItem1").assertIsDisplayed()
    composeRule.onNodeWithTag("SelectionItem1").performClick()
    assert(state.screen == DiscoverDisplayType.MAP)
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("locationButton").performClick()
  }

  @Test
  fun rangeSearchComposable_isDisplayed() {
    composeRule.activity.setContent {
      MaterialTheme {
        RangeSearchComposable(
            {},
            discoverScreenState = discoverScreenState,
            discoverScreenCallBacks = discoverScreenCallBacks)
      }
    }

    composeRule.onNodeWithTag("rangeSearch").assertIsDisplayed()
  }

  /*
   * Test for ordering options
   */
  @Test
  fun discover_sortingButtonWorks() {

    val state = discoverScreenState.copy(isLoading = false, screen = DiscoverDisplayType.LIST)

    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            discoverScreenState = state,
            discoverScreenCallBacks = discoverScreenCallBacks,
            favorites = emptyList(),
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            navigateToRangeSearch = { /*TODO*/},
            changeActivityToDisplay = {},
            changeWeatherTarget = {},
            weather = null,
            ConnectionState.CONNECTED)
      }
    }

    composeRule.onNodeWithTag("discoveryScreen").assertIsDisplayed()
    composeRule.onNodeWithTag("spinnerIcon").assertIsDisplayed()
    composeRule.onNodeWithTag("spinnerIcon").performClick()
    composeRule.onNodeWithTag("DropdownItem0").assertIsDisplayed()
    composeRule.onNodeWithTag("DropdownItem1").assertIsDisplayed()
    composeRule.onNodeWithTag("DropdownItem2").assertIsDisplayed()
    composeRule.onNodeWithTag("DropdownItem3").assertIsDisplayed()
    composeRule.onNodeWithTag("DropdownItem4").assertIsDisplayed()
    composeRule.onNodeWithTag("DropdownItem4").performClick()
  }

  @Test
  fun discoverScreenWithOrderingByDifficultyAscending() {

    val state = discoverScreenState.copy(isLoading = false, screen = DiscoverDisplayType.LIST)

    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            discoverScreenState = state,
            discoverScreenCallBacks = discoverScreenCallBacks,
            favorites = emptyList(),
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            navigateToRangeSearch = { /*TODO*/},
            changeActivityToDisplay = {},
            changeWeatherTarget = {},
            weather = null,
            ConnectionState.CONNECTED)
      }
    }
    composeRule.onNodeWithTag("textValueRow").assertIsDisplayed()
    composeRule.onNodeWithTag("spinnerIcon").assertIsDisplayed()
  }

  @Test
  fun discoverScreenWithOrderingByDifficultyDescending() {

    val state =
        discoverScreenState.copy(
            orderingBy = OrderingBy.DIFFICULTYDESCENDING,
            isLoading = false,
            screen = DiscoverDisplayType.LIST)

    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            discoverScreenState = state,
            discoverScreenCallBacks = discoverScreenCallBacks,
            favorites = emptyList(),
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            navigateToRangeSearch = { /*TODO*/},
            changeActivityToDisplay = {},
            changeWeatherTarget = {},
            weather = null,
            ConnectionState.CONNECTED)
      }
    }
    composeRule.onNodeWithTag("textValueRow").assertIsDisplayed()
    composeRule.onNodeWithTag("spinnerIcon").assertIsDisplayed()
  }

  @Test
  fun discoverScreenWithOrderingByPopularity() {

    val state =
        discoverScreenState.copy(
            orderingBy = OrderingBy.POPULARITY,
            isLoading = false,
            screen = DiscoverDisplayType.LIST)

    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            discoverScreenState = state,
            discoverScreenCallBacks = discoverScreenCallBacks,
            favorites = emptyList(),
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            navigateToRangeSearch = { /*TODO*/},
            changeActivityToDisplay = {},
            changeWeatherTarget = {},
            weather = null,
            ConnectionState.CONNECTED)
      }
    }

    composeRule.onNodeWithTag("textValueRow").assertIsDisplayed()
    composeRule.onNodeWithTag("spinnerIcon").assertIsDisplayed()
  }

  @Test
  fun discoverScreenWithOrderingByDistanceDescending() {

    val state =
        discoverScreenState.copy(
            orderingBy = OrderingBy.DISTANCE, isLoading = false, screen = DiscoverDisplayType.LIST)

    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            discoverScreenState = state,
            discoverScreenCallBacks = discoverScreenCallBacks,
            favorites = emptyList(),
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            navigateToRangeSearch = { /*TODO*/},
            changeActivityToDisplay = {},
            changeWeatherTarget = {},
            weather = null,
            ConnectionState.CONNECTED)
      }
    }
    composeRule.onNodeWithTag("textValueRow").assertIsDisplayed()
    composeRule.onNodeWithTag("spinnerIcon").assertIsDisplayed()
  }

  @Test
  fun testSuggestions() {

    val suggestions = mutableMapOf<String, LatLng>()
    var initialPos = LatLng(46.519962, 6.633597)
    var cameraPosition: CameraUpdate? = null

    composeRule.activity.setContent {
      MaterialTheme {
        MapsInitializer.initialize(AuthUI.getApplicationContext())
        HeaderComposable(
            screen = DiscoverDisplayType.LIST,
            range = 1000.0,
            selectedLocality = Pair("Ecublens", LatLng(46.519962, 6.633597)),
            fetchActivities = {},
            setScreen = { _ -> },
            navigateToRangeSearch = {},
            navigateToFilter = {},
            orderingBy = OrderingBy.RATING,
            updateOrderingBy = {},
            weather = null,
            fetchSuggestion = { suggestions["Ecublens"] = LatLng(4.519962, 6.633597) },
            suggestions = suggestions,
            setSelectedLocality = {},
            clearSuggestions = { suggestions.clear() },
            updateInitialPosition = { initialPos = it },
            { cameraPosition = it },
            ConnectionState.CONNECTED)
      }
    }

    assert(suggestions.isEmpty())

    // check that the searchbar is displayed
    composeRule.onNodeWithTag("searchBarComponent").assertIsDisplayed()
    composeRule.onNodeWithTag("searchBarComponent").performTextInput("Ecublens")

    // there should be 8 suggestions
    composeRule.onNodeWithTag("suggestion").assertIsDisplayed()
    composeRule.onNodeWithTag("suggestion").performClick()

    assert(cameraPosition != null)
    assert(initialPos != LatLng(46.519962, 6.633597))
  }

  @Test
  fun discoverScreenWithNoActivities() {
    val state =
        discoverScreenState.copy(
            isLoading = false, screen = DiscoverDisplayType.LIST, activities = emptyList())

    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            discoverScreenState = state,
            discoverScreenCallBacks = discoverScreenCallBacks,
            favorites = emptyList(),
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            navigateToRangeSearch = { /*TODO*/},
            changeActivityToDisplay = {},
            changeWeatherTarget = {},
            weather = null,
            ConnectionState.CONNECTED)
      }
    }

    composeRule.onNodeWithTag("EmptyActivityList").assertIsDisplayed()
    composeRule.onNodeWithTag("NoActivitiesLogo").assertIsDisplayed()
  }

  @Test
  fun activityImageNotNull_isDisplayed() {
    val activity =
        Activity(
            "1",
            11033919,
            ActivityType.HIKING,
            "Chemin panorama alpin",
            Position(46.4718332, 6.8338907),
            0.0f,
            0,
            emptyList(),
            difficulty = Difficulty.EASY,
            activityImageUrl =
                "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png")

    composeRule.activity.setContent {
      MaterialTheme {
        ActivitiesDisplay(
            activities = listOf(activity),
            centerPoint = LatLng(46.519962, 6.633597),
            favorites = emptyList(),
            changeActivityToDisplay = {},
            changeWeatherTarget = {},
            flipFavorite = {},
            navigateToMoreInfo = {},
            ConnectionState.CONNECTED)
      }
    }

    composeRule.onNodeWithTag("1activityCard").assertIsDisplayed()
  }
}
