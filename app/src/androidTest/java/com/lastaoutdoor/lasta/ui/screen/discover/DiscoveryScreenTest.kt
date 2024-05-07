package com.lastaoutdoor.lasta.ui.screen.discover

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.ui.screen.discover.components.RangeSearchComposable
import com.lastaoutdoor.lasta.utils.OrderingBy
import com.lastaoutdoor.lasta.viewmodel.DiscoverDisplayType
import com.lastaoutdoor.lasta.viewmodel.MapState
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

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @Test
  fun headerComposable() {
    var navigateFilter = false
    var screenType = DiscoverDisplayType.LIST
    var popUp = false
    composeRule.activity.setContent {
      MaterialTheme {
        // Assuming you have a minimal setup or mock data needed for DiscoverScreen
        HeaderComposable(
            screen = screenType,
            range = 5000.0,
            selectedLocality = "Ecublens" to LatLng(46.519962, 6.633597),
            setScreen = { screenType = it },
            updatePopup = { popUp = true },
            navigateToFilter = { navigateFilter = true },
            orderingBy = OrderingBy.RATING,
            updateOrderingBy = {},
            weather = null,
            fetchSuggestion = fetchSuggestion
        )
      }
    }

    composeRule.onNodeWithTag("header").assertIsDisplayed()
    composeRule.onNodeWithTag("locationText").assertIsDisplayed()
    composeRule.onNodeWithTag("locationButton").assertIsDisplayed()
    composeRule.onNodeWithTag("rangeText").assertIsDisplayed()
    composeRule.onNodeWithTag("searchBarComponent").assertIsDisplayed()
    composeRule.onNodeWithTag("locationButton").performClick()
    composeRule.onNodeWithTag("filterButton").performClick()
    composeRule.waitForIdle()
    assertTrue(navigateFilter)
    assertTrue(popUp)
    assertEquals(screenType, DiscoverDisplayType.LIST)
    composeRule.onNodeWithTag("sortingText").assertIsDisplayed()
    composeRule.onNodeWithTag("sortingButton").assertIsDisplayed()
    composeRule.onNodeWithTag("sortingButton").performClick()
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
            navigateToMoreInfo = {})
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
    var screenType = DiscoverDisplayType.LIST
    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            activities =
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
                        activityImageUrl = "")),
            screen = screenType,
            range = 5000.0,
            centerPoint = LatLng(46.519962, 6.633597),
            favorites = emptyList(),
            localities =
                listOf(
                    "Ecublens" to LatLng(46.519962, 6.633597),
                    "Geneva" to LatLng(46.2043907, 6.1431577),
                    "Payerne" to LatLng(46.834190, 6.928969),
                    "Matterhorn" to LatLng(45.980537, 7.641618)),
            selectedLocality = "Ecublens" to LatLng(46.519962, 6.633597),
            fetchActivities = { _, _ -> },
            setScreen = { screenType = it },
            setRange = {},
            setSelectedLocality = {},
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            changeActivityToDisplay = {},
            weather = null,
            state = MapState(),
            updatePermission = {},
            initialPosition = LatLng(46.519962, 6.633597),
            initialZoom = 11f,
            updateMarkers = { _, _ -> },
            updateSelectedMarker = { /*TODO*/},
            clearSelectedItinerary = { /*TODO*/},
            selectedZoom = 13f,
            selectedMarker = null,
            selectedItinerary = null,
            orderingBy = OrderingBy.RATING,
            updateOrderingBy = {},
            markerList = emptyList()) {}
      }
    }

    composeRule.onNodeWithTag("discoveryScreen").assertIsDisplayed()
    composeRule.onNodeWithTag("SelectionItem1").assertIsDisplayed()
    composeRule.onNodeWithTag("SelectionItem1").performClick()
    assert(screenType == DiscoverDisplayType.MAP)
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("locationButton").performClick()
    composeRule.onNodeWithTag("listSearchOptionsApplyButton").performClick()
  }

  @Test
  fun rangeSearchComposableInTypeMap_isDisplayed() {
    var isRangePopup = true
    var screenType = DiscoverDisplayType.MAP
    composeRule.activity.setContent {
      MaterialTheme {
        RangeSearchComposable(
            screen = screenType,
            localities =
                listOf(
                    "Ecublens" to LatLng(46.519962, 6.633597),
                    "Geneva" to LatLng(46.2043907, 6.1431577),
                    "Payerne" to LatLng(46.834190, 6.928969),
                    "Matterhorn" to LatLng(45.980537, 7.641618),
                ),
            selectedLocality = "Ecublens" to LatLng(46.519962, 6.633597),
            range = 5000.0,
            fetchActivities = { _, _ -> },
            onDismissRequest = { isRangePopup = false },
            setRange = {},
            isRangePopup = isRangePopup,
            setSelectedLocality = {},
        )
      }
    }

    composeRule.onNodeWithTag("rangeSearch").assertIsDisplayed()
  }

  /*
   * Test for ordering options
   */
  @Test
  fun discover_sortingButtonWorks() {
    var screenType = DiscoverDisplayType.LIST
    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            activities =
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
                        activityImageUrl = "")),
            screen = screenType,
            range = 5000.0,
            centerPoint = LatLng(46.519962, 6.633597),
            favorites = emptyList(),
            localities =
                listOf(
                    "Ecublens" to LatLng(46.519962, 6.633597),
                    "Geneva" to LatLng(46.2043907, 6.1431577),
                    "Payerne" to LatLng(46.834190, 6.928969),
                    "Matterhorn" to LatLng(45.980537, 7.641618)),
            selectedLocality = "Ecublens" to LatLng(46.519962, 6.633597),
            fetchActivities = { _, _ -> },
            setScreen = { screenType = it },
            setRange = {},
            setSelectedLocality = {},
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            changeActivityToDisplay = {},
            weather = null,
            state = MapState(),
            updatePermission = {},
            initialPosition = LatLng(46.519962, 6.633597),
            initialZoom = 11f,
            updateMarkers = { _, _ -> },
            updateSelectedMarker = { /*TODO*/},
            clearSelectedItinerary = { /*TODO*/},
            selectedZoom = 13f,
            selectedMarker = null,
            selectedItinerary = null,
            orderingBy = OrderingBy.DISTANCEASCENDING,
            updateOrderingBy = {},
            markerList = emptyList()) {}
      }
    }

    composeRule.onNodeWithTag("discoveryScreen").assertIsDisplayed()
    composeRule.onNodeWithTag("sortingButton").assertIsDisplayed()
    composeRule.onNodeWithTag("sortingButton").performClick()
    composeRule.onNodeWithTag("sortingItemRATING").assertIsDisplayed()
    composeRule.onNodeWithTag("sortingItemDISTANCEASCENDING").assertIsDisplayed()
    composeRule.onNodeWithTag("sortingItemDISTANCEDESCENDING").assertIsDisplayed()
    composeRule.onNodeWithTag("sortingItemDIFFICULTYASCENDING").assertIsDisplayed()
    composeRule.onNodeWithTag("sortingItemDIFFICULTYDESCENDING").assertIsDisplayed()
    composeRule.onNodeWithTag("sortingItemPOPULARITY").assertIsDisplayed()
    composeRule.onNodeWithTag("sortingItemPOPULARITY").performClick()
  }

  @Test
  fun discoverScreenWithOrderingByDifficultyAscending() {
    var screenType = DiscoverDisplayType.LIST
    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            activities =
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
                        activityImageUrl = "")),
            screen = screenType,
            range = 5000.0,
            centerPoint = LatLng(46.519962, 6.633597),
            favorites = emptyList(),
            localities =
                listOf(
                    "Ecublens" to LatLng(46.519962, 6.633597),
                    "Geneva" to LatLng(46.2043907, 6.1431577),
                    "Payerne" to LatLng(46.834190, 6.928969),
                    "Matterhorn" to LatLng(45.980537, 7.641618),
                ),
            selectedLocality = "Ecublens" to LatLng(46.519962, 6.633597),
            fetchActivities = { _, _ -> },
            setScreen = { screenType = it },
            setRange = {},
            setSelectedLocality = {},
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            changeActivityToDisplay = {},
            weather = null,
            state = MapState(),
            updatePermission = {},
            initialPosition = LatLng(46.519962, 6.633597),
            initialZoom = 11f,
            updateMarkers = { _, _ -> },
            updateSelectedMarker = { /*TODO*/},
            clearSelectedItinerary = { /*TODO*/},
            selectedZoom = 13f,
            selectedMarker = null,
            selectedItinerary = null,
            orderingBy = OrderingBy.DIFFICULTYASCENDING,
            updateOrderingBy = {},
            markerList = emptyList()) {}
      }
    }
    composeRule.onNodeWithTag("sortingTextValue").assertIsDisplayed()
  }

  @Test
  fun discoverScreenWithOrderingByDifficultyDescending() {
    var screenType = DiscoverDisplayType.LIST
    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            activities =
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
                        activityImageUrl = "")),
            screen = screenType,
            range = 5000.0,
            centerPoint = LatLng(46.519962, 6.633597),
            favorites = emptyList(),
            localities =
                listOf(
                    "Ecublens" to LatLng(46.519962, 6.633597),
                    "Geneva" to LatLng(46.2043907, 6.1431577),
                    "Payerne" to LatLng(46.834190, 6.928969),
                    "Matterhorn" to LatLng(45.980537, 7.641618),
                ),
            selectedLocality = "Ecublens" to LatLng(46.519962, 6.633597),
            fetchActivities = { _, _ -> },
            setScreen = { screenType = it },
            setRange = {},
            setSelectedLocality = {},
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            changeActivityToDisplay = {},
            weather = null,
            state = MapState(),
            updatePermission = {},
            initialPosition = LatLng(46.519962, 6.633597),
            initialZoom = 11f,
            updateMarkers = { _, _ -> },
            updateSelectedMarker = { /*TODO*/},
            clearSelectedItinerary = { /*TODO*/},
            selectedZoom = 13f,
            selectedMarker = null,
            selectedItinerary = null,
            orderingBy = OrderingBy.DIFFICULTYDESCENDING,
            updateOrderingBy = {},
            markerList = emptyList()) {}
      }
    }
    composeRule.onNodeWithTag("sortingTextValue").assertIsDisplayed()
  }

  @Test
  fun discoverScreenWithOrderingByPopularity() {
    var screenType = DiscoverDisplayType.LIST
    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            activities =
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
                        activityImageUrl = "")),
            screen = screenType,
            range = 5000.0,
            centerPoint = LatLng(46.519962, 6.633597),
            favorites = emptyList(),
            localities =
                listOf(
                    "Ecublens" to LatLng(46.519962, 6.633597),
                    "Geneva" to LatLng(46.2043907, 6.1431577),
                    "Payerne" to LatLng(46.834190, 6.928969),
                    "Matterhorn" to LatLng(45.980537, 7.641618),
                ),
            selectedLocality = "Ecublens" to LatLng(46.519962, 6.633597),
            fetchActivities = { _, _ -> },
            setScreen = { screenType = it },
            setRange = {},
            setSelectedLocality = {},
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            changeActivityToDisplay = {},
            weather = null,
            state = MapState(),
            updatePermission = {},
            initialPosition = LatLng(46.519962, 6.633597),
            initialZoom = 11f,
            updateMarkers = { _, _ -> },
            updateSelectedMarker = { /*TODO*/},
            clearSelectedItinerary = { /*TODO*/},
            selectedZoom = 13f,
            selectedMarker = null,
            selectedItinerary = null,
            orderingBy = OrderingBy.POPULARITY,
            updateOrderingBy = {},
            markerList = emptyList()) {}
      }
    }
    composeRule.onNodeWithTag("sortingTextValue").assertIsDisplayed()
  }

  @Test
  fun discoverScreenWithOrderingByDistanceDescending() {
    var screenType = DiscoverDisplayType.LIST
    composeRule.activity.setContent {
      MaterialTheme {
        DiscoverScreen(
            activities =
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
                        activityImageUrl = "")),
            screen = screenType,
            range = 5000.0,
            centerPoint = LatLng(46.519962, 6.633597),
            favorites = emptyList(),
            localities =
                listOf(
                    "Ecublens" to LatLng(46.519962, 6.633597),
                    "Geneva" to LatLng(46.2043907, 6.1431577),
                    "Payerne" to LatLng(46.834190, 6.928969),
                    "Matterhorn" to LatLng(45.980537, 7.641618),
                ),
            selectedLocality = "Ecublens" to LatLng(46.519962, 6.633597),
            fetchActivities = { _, _ -> },
            setScreen = { screenType = it },
            setRange = {},
            setSelectedLocality = {},
            flipFavorite = {},
            navigateToFilter = { /*TODO*/},
            navigateToMoreInfo = { /*TODO*/},
            changeActivityToDisplay = {},
            weather = null,
            state = MapState(),
            updatePermission = {},
            initialPosition = LatLng(46.519962, 6.633597),
            initialZoom = 11f,
            updateMarkers = { _, _ -> },
            updateSelectedMarker = { /*TODO*/},
            clearSelectedItinerary = { /*TODO*/},
            selectedZoom = 13f,
            selectedMarker = null,
            selectedItinerary = null,
            orderingBy = OrderingBy.DISTANCEDESCENDING,
            updateOrderingBy = {},
            markerList = emptyList()) {}
      }
    }
    composeRule.onNodeWithTag("sortingTextValue").assertIsDisplayed()
  }
}
