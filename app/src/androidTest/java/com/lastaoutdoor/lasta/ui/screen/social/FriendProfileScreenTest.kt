package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
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

  // Test that a friend profile screen with null model is displayed
  @Test
  fun friendProfileScreenIsDisplayed() {
    // composeRule.onNodeWithTag("FriendProfileScreen").assertIsNotDisplayed()
  }

  // Test that a friend profile screen with a friend model is displayed
  @Test fun friendProfileScreenWithModelIsDisplayed() {}
}
