package com.lastaoutdoor.lasta.models.activity

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import org.junit.Rule
import org.junit.Test

class ActivityTypeTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun testGetIconClimbing() {
    composeTestRule.setContent {
      androidx.compose.foundation.Image(
          painter = ActivityType.CLIMBING.getIcon(), contentDescription = "Climbing Icon")
    }
    composeTestRule.onNodeWithContentDescription("Climbing Icon").assertIsDisplayed()
  }

  @Test
  fun testGetIconHiking() {
    composeTestRule.setContent {
      androidx.compose.foundation.Image(
          painter = ActivityType.HIKING.getIcon(), contentDescription = "Hiking Icon")
    }
    composeTestRule.onNodeWithContentDescription("Hiking Icon").assertIsDisplayed()
  }

  @Test
  fun testGetIconBiking() {
    composeTestRule.setContent {
      androidx.compose.foundation.Image(
          painter = ActivityType.BIKING.getIcon(), contentDescription = "Biking Icon")
    }
    composeTestRule.onNodeWithContentDescription("Biking Icon").assertIsDisplayed()
  }
}
