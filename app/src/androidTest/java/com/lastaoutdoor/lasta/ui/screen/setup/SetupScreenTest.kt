package com.lastaoutdoor.lasta.ui.screen.setup

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.di.NetworkModule
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.ui.theme.LastaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class, NetworkModule::class)
class SetupScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @Test
  fun setupScreen_isDisplayed() {
    var userId = "123"
    var language = Language.ENGLISH
    var prefActivity = ActivityType.HIKING
    var levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)

    composeRule.activity.setContent {
      LastaTheme(darkTheme = true) {
        SetupScreen(
            userId,
            language,
            prefActivity,
            levels,
            { _ -> },
            { _ -> },
            { _ -> },
            { _ -> },
            { _ -> },
            {})
      }
    }
    composeRule.onNodeWithTag("setupScreen").assertIsDisplayed()
  }

  @Test
  fun setupScreen_changeLanguage() {
    var userId = "123"
    var language = Language.ENGLISH
    var prefActivity = ActivityType.HIKING
    var levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)

    composeRule.activity.setContent {
      LastaTheme(darkTheme = false) {
        SetupScreen(
            userId,
            language,
            prefActivity,
            levels,
            { _ -> },
            { _ -> },
            { _ -> },
            { _ -> },
            { _ -> },
            {})
      }
    }
    composeRule.onNodeWithTag("settingsLanguage").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsLanguage").performClick()
    composeRule.onNodeWithText("French").performClick()
    composeRule.onNodeWithTag("settingsLanguage").assertIsDisplayed()
  }

  @Test
  fun setupScreen_clickSubmitButton() {
    var userId = "123"
    var language = Language.ENGLISH
    var prefActivity = ActivityType.HIKING
    var levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)

    composeRule.activity.setContent {
      SetupScreen(
          userId,
          language,
          prefActivity,
          levels,
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          {},
      )
    }
    composeRule.onNodeWithTag("setupFinishButton").performClick()
    composeRule.onNodeWithTag("setupSubmitDialog").assertIsDisplayed()
  }

  /*@Test
  fun setupScreen_changeToClimbing() {
    var userId = "123"
    var language = Language.ENGLISH
    var prefActivity = ActivityType.HIKING
    var levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)

    composeRule.activity.setContent {
      SetupScreen(
          userId,
          language,
          prefActivity,
          levels,
          { _, _, _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          {})
    }
    composeRule.onNodeWithTag("setupClimbing").assertIsDisplayed()
    composeRule.onNodeWithTag("setupClimbing").performClick()
  }

  @Test
  fun setupScreen_changeToHiking() {
    var userId = "123"
    var language = Language.ENGLISH
    var prefActivity = ActivityType.HIKING
    var levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)

    composeRule.activity.setContent {
      SetupScreen(
          userId,
          language,
          prefActivity,
          levels,
          { _, _, _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          {})
    }
    composeRule.onNodeWithTag("setupHiking").assertIsDisplayed()
    composeRule.onNodeWithTag("setupHiking").performClick()
    composeRule.onNodeWithTag("setupHiking").assertIsDisplayed()
  }

  @Test
  fun setupScreen_changeToBiking() {
    var userId = "123"
    var language = Language.ENGLISH
    var prefActivity = ActivityType.HIKING
    var levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)

    composeRule.activity.setContent {
      SetupScreen(
          userId,
          language,
          prefActivity,
          levels,
          { _, _, _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          {})
    }
    composeRule.onNodeWithTag("setupBiking").assertIsDisplayed()
    composeRule.onNodeWithTag("setupBiking").performClick()
  }

  @Test
  fun setupScreen_changeHikingLevel() {
    var userId = "123"
    var language = Language.ENGLISH
    var prefActivity = ActivityType.HIKING
    var levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)

    composeRule.activity.setContent {
      SetupScreen(
          userId,
          language,
          prefActivity,
          levels,
          { _, _, _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          {})
    }
    composeRule.onNodeWithTag("setupHIKINGLevel").assertIsDisplayed()
    composeRule.onNodeWithTag("setupHIKINGLevel").performClick()
    composeRule.onNodeWithText("Intermediate").performClick()
  }

  @Test
  fun setupScreen_changeClimbingLevel() {
    var userId = "123"
    var language = Language.ENGLISH
    var prefActivity = ActivityType.HIKING
    var levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)

    composeRule.activity.setContent {
      SetupScreen(
          userId,
          language,
          prefActivity,
          levels,
          { _, _, _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          {})
    }
    composeRule.onNodeWithTag("setupCLIMBINGLevel").assertIsDisplayed()
    composeRule.onNodeWithTag("setupCLIMBINGLevel").performClick()
    composeRule.onNodeWithText("Intermediate").performClick()
  }

  @Test
  fun setupScreen_changeBikingLevel() {
    var userId = "123"
    var language = Language.ENGLISH
    var prefActivity = ActivityType.HIKING
    var levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)

    composeRule.activity.setContent {
      SetupScreen(
          userId,
          language,
          prefActivity,
          levels,
          { _, _, _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          {})
    }
    composeRule.onNodeWithTag("setupBikingLevel").assertIsDisplayed()
    composeRule.onNodeWithTag("setupBikingLevel").performClick()
    composeRule.onNodeWithText("Intermediate").performClick()
  }

  @Test
  fun setupScreen_finishButton() {
    var userId = "123"
    var language = Language.ENGLISH
    var prefActivity = ActivityType.HIKING
    var levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)

    composeRule.activity.setContent {
      SetupScreen(
          userId,
          language,
          prefActivity,
          levels,
          { _, _, _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          { _ -> },
          {})
    }
    composeRule.onNodeWithTag("setupFinishButton").performClick()
  }
   */
}
