package com.lastaoutdoor.lasta.ui.screen.moreinfo

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.ui.screen.activities.MoreInfoScreen
import com.lastaoutdoor.lasta.ui.screen.activities.fetchDiffText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class MoreInfoScreenTest {
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
    val fakeActivity = OutdoorActivity(ActivityType.CLIMBING, 1, 1.5f, "3 hours", "Test Title")
    composeRule.activity.setContent {
      val navController = rememberNavController()
      MoreInfoScreen(activity = fakeActivity, navController)
    }
  }

  @Test
  fun startButton_isDisplayed() {
    composeRule.onNodeWithTag("Start button").assertIsDisplayed()
  }

  @Test
  fun testFetchDiff() {
    val fakeActivity = OutdoorActivity(ActivityType.CLIMBING, 0, 1.5f, "3 hours", "Test Title")
    val fakeActivity2 = OutdoorActivity(ActivityType.CLIMBING, 1, 1.5f, "3 hours", "Test Title")
    val fakeActivity3 = OutdoorActivity(ActivityType.CLIMBING, 2, 1.5f, "3 hours", "Test Title")
    val fakeActivity4 = OutdoorActivity(ActivityType.CLIMBING, 3, 1.5f, "3 hours", "Test Title")
    assertEquals("Easy", fetchDiffText(activity = fakeActivity))
    assertEquals("Medium", fetchDiffText(activity = fakeActivity2))
    assertEquals("Difficult", fetchDiffText(activity = fakeActivity3))
    assertEquals("No available difficulty", fetchDiffText(activity = fakeActivity4))
  }
}
