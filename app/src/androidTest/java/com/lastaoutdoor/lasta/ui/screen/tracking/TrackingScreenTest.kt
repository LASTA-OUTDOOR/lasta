package com.lastaoutdoor.lasta.ui.screen.tracking

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
@UninstallModules(AppModule::class)
class TrackingScreenTest {
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  //  @Test
  //  fun trackingScreen_isDisplayed() {
  //    composeRule.activity.setContent {
  //      TrackingScreen(
  //          TrackingState(mockk<SensorManager>(relaxed = true)),
  //          mockk<LocationCallback>(relaxed = true),
  //          { _, _, _ -> mockk<SensorEventListener>(relaxed = true) },
  //          {})
  //    }
  //    composeRule.onNodeWithTag("TrackingScreen").assertIsDisplayed()
  //  }

  //  @Test
  //  fun location_isDisplayed() {
  //    composeRule.activity.setContent {
  //      LocationsDisplay(
  //          Modifier.fillMaxSize(),
  //          TrackingState(mockk<SensorManager>(relaxed = true)),
  //          mockk<LocationCallback>(relaxed = true))
  //    }
  //    composeRule.onNodeWithTag("LocationsDisplay").assertIsDisplayed()
  //  }
}
