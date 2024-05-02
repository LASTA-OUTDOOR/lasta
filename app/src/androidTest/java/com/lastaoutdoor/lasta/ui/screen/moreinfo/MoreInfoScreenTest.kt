package com.lastaoutdoor.lasta.ui.screen.moreinfo

import android.annotation.SuppressLint
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ClimbingActivity
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.viewmodel.MapViewModel
import com.lastaoutdoor.lasta.viewmodel.MoreInfoScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.WeatherViewModel
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

  @SuppressLint("StateFlowValueCalledInComposition")
  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      val fakeActivity = Activity("", 0L)

      val navController = rememberNavController()
      val mapViewModel: MapViewModel = hiltViewModel()
      val moreInfoScreenViewModel: MoreInfoScreenViewModel = hiltViewModel()
      val activity = ClimbingActivity("", 0)
      val weatherViewModel: WeatherViewModel = hiltViewModel()
      val weather = weatherViewModel.weather.observeAsState().value
      mapViewModel.updatePermission(true)
      MoreInfoScreen(
          activity,
          moreInfoScreenViewModel::processDiffText,
          mapViewModel.state.value,
          mapViewModel::updatePermission,
          mapViewModel.initialPosition,
          mapViewModel.initialZoom,
          mapViewModel::updateMarkers,
          mapViewModel::updateSelectedMarker,
          mapViewModel::clearSelectedItinerary,
          mapViewModel.selectedZoom,
          mapViewModel::updateSelectedItinerary,
          moreInfoScreenViewModel::goToMarker,
          weather) {
            navController.navigateUp()
          }
    }
  }

  // Test that the top bar is displayed
  @Test
  fun topBar_isDisplayed() {
    composeRule.onNodeWithTag("Top Bar").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Top Bar logo ${R.drawable.download_button}")
  }

  // Test that the more info screen is displayed
  @Test
  fun moreInfoComposable_isDisplayed() {
    // Check that the more info screen is displayed correctly
    composeRule.onNodeWithTag("MoreInfoComposable").assertIsDisplayed()
    // Check that start button is displayed
    composeRule.onNodeWithTag("MoreInfoStartButton").assertIsDisplayed()
    composeRule.onNodeWithTag("MoreInfoStartButton").performClick()
    // Check that middle zone is displayed
    composeRule.onNodeWithTag("MoreInfoMiddleZone").assertIsDisplayed()
    // Check that activity title zone is displayed
    composeRule.onNodeWithTag("MoreInfoActivityTypeComposable").assertIsDisplayed()
    composeRule.onNodeWithTag("MoreInfoActivityTypeComposable").performClick()
  }

  @Test
  fun moreInfoMapIsDisplayed() {
    composeRule.onNodeWithTag("viewOnMapButton").performClick()
  }
}
