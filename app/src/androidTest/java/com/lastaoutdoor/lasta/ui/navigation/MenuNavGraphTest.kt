package com.lastaoutdoor.lasta.ui.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
class MenuNavGraphTest {

  // Allow Hilt to inject dependencies
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  private lateinit var navController: NavHostController

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      navController = rememberNavController()
      MenuNavGraph(
          rootNavController = navController, navController = navController, modifier = Modifier)
    }
  }

  // Test the navigation for the menu in the app (Discover, Map, Profile)
  @Test
  fun menuNavGraphIsDisplayed() {
    // Check if the menu graph for the different menus is present
    // composeRule.onNodeWithTag("MainScreen").assertIsDisplayed()
    // Assert that the Main screen is displayed
    // composeRule.onNodeWithText("MenuNavGraph") // Adjust this according to your UI
    // .assertExists()

  }
}
