package com.lastaoutdoor.lasta.ui.screen.settings

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SettingsScreenKtTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  // Test that the settings screen is displayed
  @Test
  fun settingsScreenIsDisplayed() {
    composeRule.activity.setContent {
      SettingsScreen(
          language = Language.ENGLISH,
          prefActivity = ActivityType.BIKING,
          levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER),
          updateLanguage = {},
          updatePrefActivity = {},
          updateClimbingLevel = {},
          updateHikingLevel = {},
          updateBikingLevel = {},
          navigateBack = {},
          signOutAndNavigate = {})
    }
    composeRule.onNodeWithTag("settingsScreen").assertIsDisplayed()
  }

  // Test that the delete account dialog is displayed
  @Test
  fun deleteAccountDialogIsDisplayed() {
    composeRule.activity.setContent {
      SettingsScreen(
          language = Language.ENGLISH,
          prefActivity = ActivityType.BIKING,
          levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER),
          updateLanguage = {},
          updatePrefActivity = {},
          updateClimbingLevel = {},
          updateHikingLevel = {},
          updateBikingLevel = {},
          navigateBack = {},
          signOutAndNavigate = {})
    }
    composeRule.onNodeWithTag("settingsDeleteAccount").performClick()
    composeRule.onNodeWithTag("settingsDeleteDialog").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsDeleteButton").performClick()
  }

  // Test that top app bar is displayed
  @Test
  fun topAppBarIsDisplayed() {
    composeRule.activity.setContent {
      SettingsScreen(
          language = Language.ENGLISH,
          prefActivity = ActivityType.BIKING,
          levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER),
          updateLanguage = {},
          updatePrefActivity = {},
          updateClimbingLevel = {},
          updateHikingLevel = {},
          updateBikingLevel = {},
          navigateBack = {},
          signOutAndNavigate = {})
    }
    composeRule.onNodeWithTag("settingsAppBar").assertIsDisplayed()
  }

  // Test that changing language works
  @Test
  fun changeLanguageWorks() {
    var language = Language.ENGLISH
    composeRule.activity.setContent {
      SettingsScreen(
          language = language,
          prefActivity = ActivityType.BIKING,
          levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER),
          updateLanguage = { language = it },
          updatePrefActivity = {},
          updateClimbingLevel = {},
          updateHikingLevel = {},
          updateBikingLevel = {},
          navigateBack = {},
          signOutAndNavigate = {})
    }
    composeRule.onNodeWithTag("settingsLanguage").performClick()
    composeRule.onNodeWithTag("settingsLanguage").assertIsDisplayed()
  }

  // Test that changing preferred activity works
  @Test
  fun changePreferredActivityWorks() {
    var prefActivity = ActivityType.BIKING
    composeRule.activity.setContent {
      SettingsScreen(
          language = Language.ENGLISH,
          prefActivity = prefActivity,
          levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER),
          updateLanguage = {},
          updatePrefActivity = { prefActivity = it },
          updateClimbingLevel = {},
          updateHikingLevel = {},
          updateBikingLevel = {},
          navigateBack = {},
          signOutAndNavigate = {})
    }
    composeRule.onNodeWithTag("settingsFavActivity").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsHIKING").performClick()
    composeRule.onNodeWithTag("settingsHIKING").assertIsDisplayed()
  }
}
