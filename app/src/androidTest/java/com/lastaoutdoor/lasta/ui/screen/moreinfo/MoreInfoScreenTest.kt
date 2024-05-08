package com.lastaoutdoor.lasta.ui.screen.moreinfo

import android.annotation.SuppressLint
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.ui.MainActivity
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

  @SuppressLint("StateFlowValueCalledInComposition")
  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      val fakeActivity = Activity("", 0L)
      val fakeMapState = MapState()
      MoreInfoScreen(
          fakeActivity,
          fakeMapState,
          {},
          LatLng(0.0, 0.0),
          0f,
          { _, _ -> },
          {},
          {},
          0f,
          { a: Activity -> Marker(0L, "", LatLng(0.0, 0.0), "", 0, ActivityType.CLIMBING) },
          null,
          emptyList(),
          null,
          {},
          setWeatherBackToUserLoc = {})
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

  @Test
  fun elevateddiff_isDisplayed() {
    composeRule.activity.setContent {
      ElevatedDifficultyDisplay(
          activityToDisplay = Activity("", 0L, difficulty = Difficulty.NORMAL))
    }
    composeRule.onNodeWithTag("elevatedTestTag").assertIsDisplayed()
    composeRule.onNodeWithTag("elevatedTestTag").performClick()
    composeRule.activity.setContent {
      ElevatedDifficultyDisplay(activityToDisplay = Activity("", 0L, difficulty = Difficulty.HARD))
    }
    composeRule.onNodeWithTag("elevatedTestTag").assertIsDisplayed()
    composeRule.onNodeWithTag("elevatedTestTag").performClick()
  }
}
