package com.lastaoutdoor.lasta.ui.screen.tracking

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.ui.screen.tracking.components.TrackingInfo
import com.lastaoutdoor.lasta.viewmodel.TrackingState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class TrackingScreenTest {
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun testTrackingScreen() {
    composeRule.activity.setContent {
      TrackingInfo(
          trackingState = TrackingState(mockk(relaxed = true)),
          hours = "00",
          minutes = "00",
          seconds = "00")
    }
    composeRule.onNodeWithTag("TrackingInfo").assertIsDisplayed()
  }
}
