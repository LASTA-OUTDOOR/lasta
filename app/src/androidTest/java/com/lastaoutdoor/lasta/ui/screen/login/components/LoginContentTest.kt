package com.lastaoutdoor.lasta.ui.screen.login.components

import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
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
class LoginContentTest {
  // Allow Hilt to inject dependencies
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()
  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent { LoginContent {} }
  }

  // Test if discovery screen is displayed
  @Test
  fun loginContent_isDisplayed() {
    composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()
  }

  @Test
  fun logo_isDisplayed() {
    composeRule.onNodeWithTag("loginLogo").assertIsDisplayed()
  }

  @Test
  fun appname_isDisplayed() {
    composeRule.onNodeWithTag("appName").assertIsDisplayed()
  }

  @Test
  fun button_isDisplayed() {
    composeRule.onNodeWithTag("loginButton").assertIsDisplayed()
  }

  @Test
  fun pageIndicatorAreDisplayed() {
    composeRule.onNodeWithTag("pageIndicator").assertIsDisplayed()
  }

  @Test
  fun displaysRightContent() {

    var page by mutableStateOf(0)

    composeRule.activity.setContent { PagerContent(page = page) }
    // Page 0
    composeRule.onNodeWithTag("loginLogo").assertIsDisplayed()
    composeRule.onNodeWithTag("swipeText").assertIsDisplayed()
    composeRule.onNodeWithTag("activityText").assertIsNotDisplayed()
    composeRule.onNodeWithTag("activitiesExample").assertIsNotDisplayed()
    composeRule.onNodeWithTag("CommunityIcon").assertIsNotDisplayed()
    composeRule.onNodeWithTag("CommunityText").assertIsNotDisplayed()
    composeRule.onNodeWithTag("handshakeImage").assertIsNotDisplayed()
    composeRule.onNodeWithTag("JoinText").assertIsNotDisplayed()

    // Page 1
    page = 1
    composeRule.onNodeWithTag("loginLogo").assertIsNotDisplayed()
    composeRule.onNodeWithTag("swipeText").assertIsNotDisplayed()
    composeRule.onNodeWithTag("activityText").assertIsDisplayed()
    composeRule.onNodeWithTag("activitiesExample").assertIsDisplayed()
    composeRule.onNodeWithTag("CommunityIcon").assertIsNotDisplayed()
    composeRule.onNodeWithTag("CommunityText").assertIsNotDisplayed()
    composeRule.onNodeWithTag("handshakeImage").assertIsNotDisplayed()
    composeRule.onNodeWithTag("JoinText").assertIsNotDisplayed()

    // Page 2
    page = 2
    composeRule.onNodeWithTag("loginLogo").assertIsNotDisplayed()
    composeRule.onNodeWithTag("swipeText").assertIsNotDisplayed()
    composeRule.onNodeWithTag("activityText").assertIsNotDisplayed()
    composeRule.onNodeWithTag("activitiesExample").assertIsNotDisplayed()
    composeRule.onNodeWithTag("CommunityIcon").assertIsDisplayed()
    composeRule.onNodeWithTag("CommunityText").assertIsDisplayed()
    composeRule.onNodeWithTag("handshakeImage").assertIsNotDisplayed()
    composeRule.onNodeWithTag("JoinText").assertIsNotDisplayed()

    // Page 3
    page = 3

    composeRule.onNodeWithTag("loginLogo").assertIsNotDisplayed()
    composeRule.onNodeWithTag("swipeText").assertIsNotDisplayed()
    composeRule.onNodeWithTag("activityText").assertIsNotDisplayed()
    composeRule.onNodeWithTag("activitiesExample").assertIsNotDisplayed()
    composeRule.onNodeWithTag("CommunityIcon").assertIsNotDisplayed()
    composeRule.onNodeWithTag("CommunityText").assertIsNotDisplayed()
    composeRule.onNodeWithTag("handshakeImage").assertIsDisplayed()
    composeRule.onNodeWithTag("JoinText").assertIsDisplayed()
  }
}
