package com.lastaoutdoor.lasta.ui.screen.moreinfo

import android.annotation.SuppressLint
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.weather.Main
import com.lastaoutdoor.lasta.data.api.weather.MainForecast
import com.lastaoutdoor.lasta.data.api.weather.WeatherForecast
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.data.api.weather.Wind
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenCallBacks
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenState
import com.lastaoutdoor.lasta.viewmodel.MapState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class MoreInfoScreenTest {
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  val discoverScreenCallBacks =
      DiscoverScreenCallBacks(
          fetchActivities = {},
          setScreen = {},
          setSelectedLocality = {},
          updatePermission = {},
          updateMarkers = { _, _ -> },
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

  @SuppressLint("StateFlowValueCalledInComposition")
  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      val fakeMapState = MapState()
      val activities = emptyList<Activity>()
      val currentUser = UserModel("")
      val fakeRatings = listOf(Rating("123", "genial", "5"))
      val fakeUsersList = listOf(UserModel("123"))
      val fakeActivity = Activity("", 0L, ratings = fakeRatings)

      val discoverScreenState =
          DiscoverScreenState(isLoading = false, selectedActivityTypes = emptyList())

      MoreInfoScreen(
          activityToDisplay = fakeActivity,
          discoverScreenState = discoverScreenState,
          discoverScreenCallBacks = discoverScreenCallBacks,
          goToMarker = { _ -> Marker(2, "", LatLng(0.0, 0.0), "", 0, ActivityType.HIKING) },
          usersList = fakeUsersList,
          getUserModels = { _ -> },
          writeNewRating = { _, _, _ -> },
          updateDifficulty = {},
          currentUser = currentUser,
          weather =
              WeatherResponse(
                  "Paris", main = Main(0.0, 0.0), weather = emptyList(), wind = Wind(0.0)),
          shareToFriend = { _, _ -> },
          friends = emptyList(),
          favorites = emptyList(),
          weatherForecast =
              WeatherForecast(
                  main = MainForecast(0.0, 0.0, 0.0, 0.0), weather = emptyList(), dt = ""),
          dateWeatherForecast = "",
          flipFavorite = { _ -> },
          navigateBack = { /*TODO*/},
          navigateToTracking = {},
          downloadActivity = {},
          setWeatherBackToUserLoc = {},
          clearSelectedMarker = {})
    }
  }

  @Test
  fun clickOnWeather() {
    composeRule.onNodeWithTag("WeatherReportBig").performClick()
    composeRule.onNodeWithTag("forecast").assertIsDisplayed()
  }

  // Test that the top bar is displayed
  @Test
  fun topBar_isDisplayedAndClicked() {
    composeRule.onNodeWithTag("Top Bar").assertIsDisplayed()
    // download button
    composeRule
        .onNodeWithContentDescription("Top Bar logo ${R.drawable.download_button}")
        .assertIsDisplayed()
    composeRule
        .onNodeWithContentDescription("Top Bar logo ${R.drawable.download_button}")
        .performClick()

    // back button
    composeRule
        .onNodeWithContentDescription("Top Bar logo ${R.drawable.arrow_back}")
        .assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Top Bar logo ${R.drawable.arrow_back}").performClick()

    // favorite button
    composeRule.onNodeWithContentDescription("Top Bar logo fav").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Top Bar logo fav").performClick()

    // share button
    composeRule.onNodeWithContentDescription("Top Bar logo ${R.drawable.share}").assertIsDisplayed()

    composeRule.onNodeWithContentDescription("Top Bar logo ${R.drawable.share}").performClick()
  }

  // Test that the more info screen is displayed.
  @Test
  fun moreInfoComposable_isDisplayed() {
    // Check that the more info screen is displayed correctly
    composeRule.onNodeWithTag("MoreInfoComposable").assertIsDisplayed()
    // Check that start button is displayed
    composeRule.onNodeWithTag("MoreInfoStartButton").assertIsDisplayed()
    composeRule.onNodeWithTag("MoreInfoStartButton").performClick()
    // Check that middle zone is displayed
    composeRule.onNodeWithTag("MoreInfoMiddleZone").assertIsDisplayed()
  }

  @Test
  fun ratingCard_isDisplayed() {
    composeRule.onNodeWithTag("RatingCard").assertIsDisplayed()
  }

  @Test
  fun moreInfoMapIsDisplayed() {
    composeRule.onNodeWithTag("viewOnMapButton").performClick()
  }

  @Test
  fun bottomSheet_isDisplayed() {
    composeRule.onNodeWithTag("AddRatingButton").assertIsDisplayed()
    composeRule.onNodeWithTag("AddRatingButton").performClick()
    composeRule.onNodeWithTag("ModalBottomSheet").assertIsDisplayed()
    composeRule.onNodeWithTag("StarButtons").assertIsDisplayed()
    composeRule.onNodeWithTag("StarButtons").performClick()
    composeRule.onNodeWithTag("PublishButton").assertIsDisplayed()
    composeRule.onNodeWithTag("PublishButton").performClick()
  }

  @Test
  fun elevateDiff_isDisplayed() {
    composeRule.activity.setContent {
      ElevatedDifficultyDisplay(
          updateDifficulty = {},
          activityToDisplay = Activity("", 0L, difficulty = Difficulty.NORMAL))
    }
    composeRule.onNodeWithTag("elevatedTestTag").assertIsDisplayed()
    composeRule.onNodeWithTag("elevatedTestTag").performClick()
    composeRule.activity.setContent {
      ElevatedDifficultyDisplay(
          updateDifficulty = {}, activityToDisplay = Activity("", 0L, difficulty = Difficulty.HARD))
    }
    composeRule.onNodeWithTag("elevatedTestTag").assertIsDisplayed()
    composeRule.onNodeWithTag("elevatedTestTag").performClick()
  }

  // Test top bar with favorites has a filled heart
  @Test
  fun topBarWithFavorites_isDisplayed() {
    composeRule.activity.setContent {
      TopBar(
          activityToDisplay = Activity("3", osmId = 2),
          downloadActivity = {},
          favorites = listOf("3"),
          navigateBack = {},
          flipFavorite = {},
          friends = emptyList(),
          shareToFriend = { _, _ -> })
    }
    composeRule.onNodeWithTag("Top Bar").assertIsDisplayed()
  }

  // Test ShareOptionsDialog
  @Test
  fun shareOptionsDialog_isDisplayed() {
    composeRule.onNodeWithContentDescription("Top Bar logo ${R.drawable.share}").performClick()
    composeRule.onNodeWithTag("shareOptionsDialog").assertIsDisplayed()
    composeRule.onNodeWithTag("closeShareOptionsButton").performClick()
    composeRule.onNodeWithTag("shareOptionsDialog").assertIsNotDisplayed()
  }

  // Test ShareOptionsDialog with shareOutsideButton
  @Test
  fun shareOptionsDialog_shareOutsideButton_isDisplayed() {
    composeRule.onNodeWithContentDescription("Top Bar logo ${R.drawable.share}").performClick()
    composeRule.onNodeWithTag("shareOutsideButton").assertIsDisplayed()
    composeRule.onNodeWithTag("shareOutsideButton").performClick()
  }

  // Test ShareOptionsDialog with shareInAppButton
  @Test
  fun shareOptionsDialog_shareInAppButton_isDisplayed() {
    composeRule.onNodeWithContentDescription("Top Bar logo ${R.drawable.share}").performClick()
    composeRule.onNodeWithTag("shareInAppButton").assertIsDisplayed()
    composeRule.onNodeWithTag("shareInAppButton").performClick()
    composeRule.onNodeWithTag("friendSharePicker").assertIsDisplayed()
  }

  @Test
  fun moreInfoScreen_withHikingActivity() {
    composeRule.activity.setContent {
      val fakeMapState = MapState()
      val activities = emptyList<Activity>()
      val currentUser = UserModel("")
      val fakeRatings = listOf(Rating("123", "genial", "5"))
      val fakeUsersList = listOf(UserModel("123"))
      val fakeActivity = Activity("", 0L, ratings = fakeRatings, activityType = ActivityType.HIKING)

      val discoverScreenState =
          DiscoverScreenState(isLoading = false, selectedActivityTypes = emptyList())

      MoreInfoScreen(
          activityToDisplay = fakeActivity,
          discoverScreenState = discoverScreenState,
          discoverScreenCallBacks = discoverScreenCallBacks,
          goToMarker = { _ -> Marker(2, "", LatLng(0.0, 0.0), "", 0, ActivityType.HIKING) },
          usersList = fakeUsersList,
          getUserModels = { _ -> },
          writeNewRating = { _, _, _ -> },
          updateDifficulty = {},
          currentUser = currentUser,
          weather =
              WeatherResponse(
                  "Paris", main = Main(0.0, 0.0), weather = emptyList(), wind = Wind(0.0)),
          shareToFriend = { _, _ -> },
          friends = emptyList(),
          favorites = emptyList(),
          weatherForecast =
              WeatherForecast(
                  main = MainForecast(0.0, 0.0, 0.0, 0.0), weather = emptyList(), dt = ""),
          dateWeatherForecast = "",
          flipFavorite = { _ -> },
          navigateBack = { /*TODO*/},
          navigateToTracking = {},
          downloadActivity = {},
          setWeatherBackToUserLoc = {},
          clearSelectedMarker = {})
    }
    composeRule.onNodeWithTag("MoreInfoComposable").assertIsDisplayed()
    composeRule.onNodeWithTag("HikingPicture").assertIsDisplayed()
  }

  @Test
  fun moreInfoScreen_withBikingActivity() {
    composeRule.activity.setContent {
      val fakeMapState = MapState()
      val activities = emptyList<Activity>()
      val currentUser = UserModel("")
      val fakeRatings = listOf(Rating("123", "genial", "5"))
      val fakeUsersList = listOf(UserModel("123"))
      val fakeActivity = Activity("", 0L, ratings = fakeRatings, activityType = ActivityType.BIKING)

      val discoverScreenState =
          DiscoverScreenState(isLoading = false, selectedActivityTypes = emptyList())

      MoreInfoScreen(
          activityToDisplay = fakeActivity,
          discoverScreenState = discoverScreenState,
          discoverScreenCallBacks = discoverScreenCallBacks,
          goToMarker = { _ -> Marker(2, "", LatLng(0.0, 0.0), "", 0, ActivityType.HIKING) },
          usersList = fakeUsersList,
          getUserModels = { _ -> },
          writeNewRating = { _, _, _ -> },
          updateDifficulty = {},
          currentUser = currentUser,
          weather =
              WeatherResponse(
                  "Paris", main = Main(0.0, 0.0), weather = emptyList(), wind = Wind(0.0)),
          shareToFriend = { _, _ -> },
          friends = emptyList(),
          favorites = emptyList(),
          weatherForecast =
              WeatherForecast(
                  main = MainForecast(0.0, 0.0, 0.0, 0.0), weather = emptyList(), dt = ""),
          dateWeatherForecast = "",
          flipFavorite = { _ -> },
          navigateBack = { /*TODO*/},
          navigateToTracking = {},
          downloadActivity = {},
          setWeatherBackToUserLoc = {},
          clearSelectedMarker = {})
    }
    composeRule.onNodeWithTag("MoreInfoComposable").assertIsDisplayed()
    composeRule.onNodeWithTag("BikingPicture").assertIsDisplayed()
  }
}
