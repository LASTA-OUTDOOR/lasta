package com.lastaoutdoor.lasta.ui.screen.settings

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
class SettingsScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @Test
  fun settingsScreenTopBar_isDisplayed() {
    val language = Language.ENGLISH
    val prefActivity = ActivityType.HIKING
    val levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)
    composeRule.activity.setContent {
      LastaTheme(darkTheme = true) {
        SettingsScreen(
            language,
            prefActivity,
            levels,
            { _ -> },
            { _ -> },
            { _ -> },
            { _ -> },
            { _ -> },
            {},
            {})
      }
    }
    composeRule.onNodeWithTag("TopBarLogo").assertIsDisplayed()
  }

  @Test
  fun settingsScreen_isDisplayed() {
    val language = Language.ENGLISH
    val prefActivity = ActivityType.HIKING
    val levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)
    composeRule.activity.setContent {
      LastaTheme(darkTheme = false) {
        SettingsScreen(
            language,
            prefActivity,
            levels,
            { _ -> },
            { _ -> },
            { _ -> },
            { _ -> },
            { _ -> },
            {},
            {})
      }
    }

    composeRule.onNodeWithTag("settingsScreen").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsTitle").assertIsDisplayed()
  }

  @Test
  fun settingsScreen_changeLanguage() {
    val language = Language.ENGLISH
    val prefActivity = ActivityType.HIKING
    val levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)
    composeRule.activity.setContent {
      SettingsScreen(
          language, prefActivity, levels, { _ -> }, { _ -> }, { _ -> }, { _ -> }, { _ -> }, {}, {})
    }
    composeRule.onNodeWithTag("settingsLanguage").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsLanguage").performClick()
    composeRule.onNodeWithText("French").performClick()
    composeRule.onNodeWithTag("settingsLanguage").assertIsDisplayed()
  }

  @Test
  fun settingsScreen_changeToHiking() {
    val language = Language.ENGLISH
    val prefActivity = ActivityType.HIKING
    val levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)
    composeRule.activity.setContent {
      SettingsScreen(
          language, prefActivity, levels, { _ -> }, { _ -> }, { _ -> }, { _ -> }, { _ -> }, {}, {})
    }
    composeRule.onNodeWithTag("settingsHiking").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsHiking").performClick()
    composeRule.onNodeWithTag("settingsHiking").assertIsDisplayed()
  }

  @Test
  fun settingsScreen_changeToClimbing() {
    val language = Language.ENGLISH
    val prefActivity = ActivityType.HIKING
    val levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)
    composeRule.activity.setContent {
      SettingsScreen(
          language, prefActivity, levels, { _ -> }, { _ -> }, { _ -> }, { _ -> }, { _ -> }, {}, {})
    }
    composeRule.onNodeWithTag("settingsClimbing").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsClimbing").performClick()
    composeRule.onNodeWithTag("settingsClimbing").assertIsDisplayed()
  }

  @Test
  fun settingsScreen_changeToBiking() {
    val language = Language.ENGLISH
    val prefActivity = ActivityType.HIKING
    val levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)
    composeRule.activity.setContent {
      SettingsScreen(
          language, prefActivity, levels, { _ -> }, { _ -> }, { _ -> }, { _ -> }, { _ -> }, {}, {})
    }
    composeRule.onNodeWithTag("settingsBiking").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsBiking").performClick()
    composeRule.onNodeWithTag("settingsBiking").assertIsDisplayed()
  }

  @Test
  fun settingsScreen_changeHikingLevel() {
    val language = Language.ENGLISH
    val prefActivity = ActivityType.HIKING
    val levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)
    composeRule.activity.setContent {
      SettingsScreen(
          language, prefActivity, levels, { _ -> }, { _ -> }, { _ -> }, { _ -> }, { _ -> }, {}, {})
    }
    composeRule.onNodeWithTag("settingsHIKINGLevel").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsHIKINGLevel").performClick()
    composeRule.onNodeWithText("Intermediate").performClick()
  }

  @Test
  fun settingsScreen_changeClimbingLevel() {
    val language = Language.ENGLISH
    val prefActivity = ActivityType.HIKING
    val levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)
    composeRule.activity.setContent {
      SettingsScreen(
          language, prefActivity, levels, { _ -> }, { _ -> }, { _ -> }, { _ -> }, { _ -> }, {}, {})
    }
    composeRule.onNodeWithTag("settingsCLIMBINGLevel").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsCLIMBINGLevel").performClick()
    composeRule.onNodeWithText("Intermediate").performClick()
  }

  @Test
  fun settingsScreen_changeBikingLevel() {
    val language = Language.ENGLISH
    val prefActivity = ActivityType.HIKING
    val levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)
    composeRule.activity.setContent {
      SettingsScreen(
          language, prefActivity, levels, { _ -> }, { _ -> }, { _ -> }, { _ -> }, { _ -> }, {}, {})
    }
    composeRule.onNodeWithTag("settingsBIKINGLevel").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsBIKINGLevel").performClick()
    composeRule.onNodeWithText("Intermediate").performClick()
  }

  @Test
  fun settingsScreen_signOut() {
    val language = Language.ENGLISH
    val prefActivity = ActivityType.HIKING
    val levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)
    composeRule.activity.setContent {
      SettingsScreen(
          language, prefActivity, levels, { _ -> }, { _ -> }, { _ -> }, { _ -> }, { _ -> }, {}, {})
    }
    composeRule.onNodeWithTag("settingsSignOut").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsSignOut").performClick()
  }

  @Test
  fun settingsScreen_deleteAccount() {
    val language = Language.ENGLISH
    val prefActivity = ActivityType.HIKING
    val levels = UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)
    composeRule.activity.setContent {
      SettingsScreen(
          language, prefActivity, levels, { _ -> }, { _ -> }, { _ -> }, { _ -> }, { _ -> }, {}, {})
    }
    composeRule.onNodeWithTag("settingsDeleteAccount").assertIsDisplayed()
    composeRule.onNodeWithTag("settingsDeleteAccount").performClick()
  }
}
