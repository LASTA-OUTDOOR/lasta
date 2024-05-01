package com.lastaoutdoor.lasta.ui.screen.login.components

import com.lastaoutdoor.lasta.di.AppModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

@HiltAndroidTest
@UninstallModules(AppModule::class)
class LoginContentTest {
  // Allow Hilt to inject dependencies
  /*@get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

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
  }*/
}
