package com.lastaoutdoor.lasta.ui.components

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertNotEquals
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
    composeRule.activity.setContent { StepsTakenField(globalStepCount = 0) }
    composeRule.onNodeWithTag("StepCountTag").assertIsDisplayed()
    composeRule.onNodeWithTag("GlobalStepCountTag").assertIsDisplayed()
  }

  @Test
  fun unavailableStepCounter_isDisplayed() {
    composeRule.activity.setContent { UnavailableStepCounter() }
    composeRule.onNodeWithTag("UnavailableStepCounterTag").assertIsDisplayed()
  }

  @Test
  fun stepCountDisplay_isDisplayed() {
    composeRule.activity.setContent { StepCountDisplay(null) }
    composeRule.onNodeWithTag("StepCountDisplayTag").assertIsNotDisplayed()
  }

  @Test
  fun registerSensorListener_ifWorking() {
    assertNotEquals(null, registerSensorListener(null, null) {})
  }
}
