package com.lastaoutdoor.lasta.ui.screen.setup

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class SetupScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()

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
  }

  @Test
  fun setupScreen_isDisplayed() {
    composeRule.onNodeWithTag("setupScreen").assertIsDisplayed()
  }
}
