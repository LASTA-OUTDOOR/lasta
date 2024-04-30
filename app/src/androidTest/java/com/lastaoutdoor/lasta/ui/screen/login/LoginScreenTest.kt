package com.lastaoutdoor.lasta.ui.screen.login

import com.lastaoutdoor.lasta.di.AppModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

@HiltAndroidTest
@UninstallModules(AppModule::class)
class LoginScreenTest {
  // Allow Hilt to inject dependencies
  /*@get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      LoginScreen(
          navController = rememberNavController(), rootNavController = rememberNavController())
    }
  }

  @Test
  fun loginScreen_isdisplayed() {
    composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()
  }*/
}
