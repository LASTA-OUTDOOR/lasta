package com.lastaoutdoor.lasta.ui.screen.profile

import com.lastaoutdoor.lasta.di.AppModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

@HiltAndroidTest
@UninstallModules(AppModule::class)
class ProfileScreenTest {

  /*@get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      val rootNavController = rememberNavController()
      ProfileScreen(rootNavController = rootNavController, navController = rootNavController)
    }
  }*/

  /**
   * @Test fun profileScreenIsDisplayed() {
   *   composeRule.onNodeWithTag("ProfileScreen").assertIsDisplayed() }
   */
  /**
   * @Test fun testSpinner() { composeRule.onNodeWithTag("spinnerIcon").performClick()
   *   composeRule.onNodeWithTag("DropdownItem1").performClick()
   *   composeRule.onNodeWithTag("TestClimb").assertIsDisplayed() }
   */
}
