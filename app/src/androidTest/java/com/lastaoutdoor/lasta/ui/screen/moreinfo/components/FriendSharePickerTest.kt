package com.lastaoutdoor.lasta.ui.screen.moreinfo.components

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class FriendSharePickerTest {
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  // Test to check if the FriendSharePicker composable is displayed
  @Test
  fun friendSharePickerTest() {
    composeRule.activity.setContent {
      FriendSharePicker(
          activityShared =
              Activity(
                  "id",
                  1,
                  ActivityType.BIKING,
                  "activityImageUrl",
                  Position(0.0, 0.0),
                  2f,
                  3,
              ),
          friends = listOf(UserModel("1")),
          hideFriendPicker = {},
          shareToFriend = { _, _ -> })
    }
    composeRule.onNodeWithTag("friendSharePicker").assertIsDisplayed()
    composeRule.onNodeWithTag("shareTo1").performClick()
    composeRule.onNodeWithTag("friendSharePicker").assertIsDisplayed()
  }
}
