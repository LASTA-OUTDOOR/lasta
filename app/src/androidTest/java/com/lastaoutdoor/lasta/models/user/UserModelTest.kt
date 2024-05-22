package com.lastaoutdoor.lasta.models.user

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.ActivityType
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class UserModelTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun testdescrText() {
    val userModel = UserModel(userId = "")
    composeTestRule.setContent { Text(text = userModel.descrText()) }

    composeTestRule.onNodeWithText("Climbing Beginner").assertIsDisplayed()
  }

  @Test
  fun testdescrTextIntermediate() {
    val userModel =
        UserModel(
            userId = "",
            prefActivity = ActivityType.CLIMBING,
            levels =
                UserActivitiesLevel(UserLevel.INTERMEDIATE, UserLevel.BEGINNER, UserLevel.BEGINNER))
    composeTestRule.setContent { Text(text = userModel.descrText()) }

    composeTestRule.onNodeWithText("Climbing Amateur").assertIsDisplayed()
  }

  @Test
  fun testdescrTextClimbingAdvanced() {
    val userModel =
        UserModel(
            userId = "",
            prefActivity = ActivityType.CLIMBING,
            levels =
                UserActivitiesLevel(UserLevel.ADVANCED, UserLevel.ADVANCED, UserLevel.BEGINNER))
    composeTestRule.setContent { Text(text = userModel.descrText()) }

    composeTestRule.onNodeWithText("Climbing Expert").assertIsDisplayed()
  }

  @Test
  fun testdescrTextHiking() {
    val userModel =
        UserModel(
            userId = "",
            prefActivity = ActivityType.HIKING,
            levels =
                UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.INTERMEDIATE, UserLevel.BEGINNER))
    composeTestRule.setContent { Text(text = userModel.descrText()) }

    composeTestRule.onNodeWithText("Hiking Amateur").assertIsDisplayed()
  }

  @Test
  fun testdescrTextHikingBeginner() {
    val userModel =
        UserModel(
            userId = "",
            prefActivity = ActivityType.HIKING,
            levels =
                UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER))
    composeTestRule.setContent { Text(text = userModel.descrText()) }

    composeTestRule.onNodeWithText("Hiking Beginner").assertIsDisplayed()
  }

  @Test
  fun testdescrTextHikingAdvanced() {
    val userModel =
        UserModel(
            userId = "",
            prefActivity = ActivityType.HIKING,
            levels =
                UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.ADVANCED, UserLevel.BEGINNER))
    composeTestRule.setContent { Text(text = userModel.descrText()) }

    composeTestRule.onNodeWithText("Hiking Expert").assertIsDisplayed()
  }

  @Test
  fun testdescrTextBiking() {
    val userModel =
        UserModel(
            userId = "",
            prefActivity = ActivityType.BIKING,
            levels =
                UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.INTERMEDIATE))
    composeTestRule.setContent { Text(text = userModel.descrText()) }

    composeTestRule.onNodeWithText("Biking Amateur").assertIsDisplayed()
  }

  @Test
  fun testdescrTextBikingAdvanced() {
    val userModel =
        UserModel(
            userId = "",
            prefActivity = ActivityType.BIKING,
            levels =
                UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.ADVANCED))
    composeTestRule.setContent { Text(text = userModel.descrText()) }

    composeTestRule.onNodeWithText("Biking Expert").assertIsDisplayed()
  }

  @Test
  fun testdescrTextBikingIntermediate() {
    val userModel =
        UserModel(
            userId = "",
            prefActivity = ActivityType.BIKING,
            levels =
                UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER))
    composeTestRule.setContent { Text(text = userModel.descrText()) }

    composeTestRule.onNodeWithText("Biking Beginner").assertIsDisplayed()
  }
}
