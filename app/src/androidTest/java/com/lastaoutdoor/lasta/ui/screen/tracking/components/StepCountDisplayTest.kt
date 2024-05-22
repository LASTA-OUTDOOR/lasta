package com.lastaoutdoor.lasta.ui.screen.tracking.components

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class StepCountDisplayTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @Test
  fun stepsTakenField_isDisplayed() {
    composeRule.activity.setContent {
      StepsTakenField(globalStepCount = 0, modifier = Modifier.fillMaxSize())
    }
    composeRule.onNodeWithTag("StepCountTag").assertIsDisplayed()
    composeRule.onNodeWithTag("GlobalStepCountTag").assertIsDisplayed()
  }

  @Test
  fun unavailableStepCounter_isDisplayed() {
    composeRule.activity.setContent { UnavailableStepCounter(modifier = Modifier.fillMaxSize()) }
    composeRule.onNodeWithTag("UnavailableStepCounterTag").assertIsDisplayed()
  }
}
