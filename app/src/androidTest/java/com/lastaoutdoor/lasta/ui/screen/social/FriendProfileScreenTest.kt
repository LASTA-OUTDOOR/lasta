package com.lastaoutdoor.lasta.ui.screen.social

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.ActivityType
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
class FriendProfileScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
  }

  // Test that a friend profile screen with no activities is displayed
  @Test
  fun friendProfileScreenIsDisplayed() {
    composeRule.activity.setContent {
      FriendProfileScreen(
          activities = emptyList(),
          timeFrame = TimeFrame.ALL,
          sport = ActivityType.BIKING,
          isCurrentUser = false,
          user = UserModel("1"),
          setSport = {},
          setTimeFrame = {},
          navigateToSettings = {},
          onBack = {},
          ConnectionState.CONNECTED)
    }
    composeRule.onNodeWithTag("FriendProfileHeader").assertIsDisplayed()
  }
}
