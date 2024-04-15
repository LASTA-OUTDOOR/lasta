package com.lastaoutdoor.lasta.ui.navigation

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
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
  private lateinit var rootNavController: NavHostController

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      rootNavController = rememberNavController()
      navController = rememberNavController()
      MenuNavGraph(
          rootNavController = rootNavController,
          navController = navController,
          modifier = Modifier.fillMaxSize())
    }
  }

  // Test the navigation for the menu in the app (Discover, Map, Profile)
  @Test
  fun menuNavGraphIsDisplayed() {
    composeRule.onNodeWithTag("MenuNavGraph").assertIsDisplayed()
  }
}
