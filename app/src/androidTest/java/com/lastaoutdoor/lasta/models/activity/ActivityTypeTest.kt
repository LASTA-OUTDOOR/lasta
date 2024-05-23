package com.lastaoutdoor.lasta.models.activity

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.lastaoutdoor.lasta.di.AppModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class ActivityTypeTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun test_GetIconClimbing() {
    composeTestRule.setContent {
      androidx.compose.foundation.Image(
          painter = ActivityType.CLIMBING.getIcon(), contentDescription = "Climbing Icon")
    }
    composeTestRule.onNodeWithContentDescription("Climbing Icon").assertIsDisplayed()
  }

  @Test
  fun test_GetIconHiking() {
    composeTestRule.setContent {
      androidx.compose.foundation.Image(
          painter = ActivityType.HIKING.getIcon(), contentDescription = "Hiking Icon")
    }
    composeTestRule.onNodeWithContentDescription("Hiking Icon").assertIsDisplayed()
  }

  @Test
  fun test_GetIconBiking() {
    composeTestRule.setContent {
      androidx.compose.foundation.Image(
          painter = ActivityType.BIKING.getIcon(), contentDescription = "Biking Icon")
    }
    composeTestRule.onNodeWithContentDescription("Biking Icon").assertIsDisplayed()
  }
}
