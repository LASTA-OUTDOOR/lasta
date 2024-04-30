package com.lastaoutdoor.lasta.ui.screen.moreinfo

import com.lastaoutdoor.lasta.di.AppModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

@HiltAndroidTest
@UninstallModules(AppModule::class)
class MoreInfoScreenTest {
  /*@get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      val navController = rememberNavController()
      MoreInfoScreen(navController = navController, hiltViewModel())
    }
  }

  // Test that the top bar is displayed
  @Test
  fun topBar_isDisplayed() {
    composeRule.onNodeWithTag("MoreInfoTopBar").assertIsDisplayed()
  }

  // Test that the more info screen is displayed
  @Test
  fun moreInfoComposable_isDisplayed() {
    // Check that the more info screen is displayed correctly
    composeRule.onNodeWithTag("MoreInfoComposable").assertIsDisplayed()
    // Check that start button is displayed
    composeRule.onNodeWithTag("MoreInfoStartButton").assertIsDisplayed()
    composeRule.onNodeWithTag("MoreInfoStartButton").performClick()
    // Check that middle zone is displayed
    composeRule.onNodeWithTag("MoreInfoMiddleZone").assertIsDisplayed()
    // Check that activity title zone is displayed
    composeRule.onNodeWithTag("MoreInfoActivityTitleZone").assertIsDisplayed()
    composeRule.onNodeWithTag("MoreInfoActivityTypeComposable").assertIsDisplayed()
  }*/
}
