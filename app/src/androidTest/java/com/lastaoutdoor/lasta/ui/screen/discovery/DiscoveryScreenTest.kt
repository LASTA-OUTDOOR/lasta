package com.lastaoutdoor.lasta.ui.screen.discovery

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.ui.theme.LastaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class DiscoveryScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.setContent {
      val navController = rememberNavController()
      LastaTheme {
        NavHost(navController = navController, startDestination = "DiscoveryScreen") {
          composable(route = "DiscoveryScreen") { DiscoveryScreen() }
        }
      }
    }
  }

  @Test
  fun discoveryScreen_isDisplayed() {
    composeRule.onNodeWithText("Discovery").assertIsDisplayed()
  }
}
