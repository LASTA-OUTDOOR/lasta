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

  @Test
  fun topBar_isDisplayed() {
    composeRule.onNodeWithTag("Top Bar").assertIsDisplayed()
  }*/
}
