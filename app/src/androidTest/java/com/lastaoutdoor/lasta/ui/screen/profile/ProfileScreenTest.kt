package com.lastaoutdoor.lasta.ui.screen.profile

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.BikingUserActivity
import com.lastaoutdoor.lasta.models.user.HikingUserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.TimeFrame
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class ProfileScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  private lateinit var userModel: UserModel

  @Before
  fun setUp() {
    userModel = UserModel("")
    hiltRule.inject()
    composeRule.activity.setContent {
      ProfileScreen(
          listOf(),
          TimeFrame.M,
          ActivityType.CLIMBING,
          true,
          userModel,
          {},
          {},
          {},
          {},
          ConnectionState.CONNECTED)
    }
  }

  @Test
  fun profileScreenIsDisplayed() {
    composeRule.onNodeWithTag("ProfileScreen").assertIsDisplayed()
    composeRule.onNodeWithTag("showDialog").assertIsDisplayed()
  }

  @Test
  fun testSpinner() {
    composeRule.onNodeWithTag("spinnerIcon").performClick()
    composeRule.onNodeWithTag("DropdownItem1").performClick()
    composeRule.onNodeWithTag("TestClimb").assertIsDisplayed()
  }

  @Test
  fun changeBio_isDisplayed() {
    composeRule.activity.setContent {
      ChangeBio(isEditBio = true, onDismissRequest = {}, bioText = "adfs") {}
    }
    composeRule.onNodeWithTag("ProfileModal").assertIsDisplayed()
    composeRule.onNodeWithTag("ProfileBox").assertIsDisplayed()
    composeRule.onNodeWithText("Save").performClick()
    composeRule.onNodeWithTag("ProfileTextField").assertIsDisplayed()
  }

  @Test
  fun chart_isDisplayed() {
    composeRule.activity.setContent {
      Chart(
          activities = listOf(BikingUserActivity()),
          timeFrame = TimeFrame.W,
          sport = ActivityType.HIKING)
    }
    composeRule.onNodeWithTag("TestHike").assertIsDisplayed()
    composeRule.activity.setContent {
      Chart(activities = listOf(), timeFrame = TimeFrame.Y, sport = ActivityType.HIKING)
    }
    composeRule.onNodeWithTag("TestHike").assertIsDisplayed()
    composeRule.activity.setContent {
      Chart(
          activities = listOf(HikingUserActivity()),
          timeFrame = TimeFrame.ALL,
          sport = ActivityType.HIKING)
    }
    composeRule.onNodeWithTag("TestHike").assertIsDisplayed()
  }
}
