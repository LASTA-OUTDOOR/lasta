package com.lastaoutdoor.lasta.ui.screen.discovery

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.composable
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
class DiscoveryScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()


  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      val navController = androidx.navigation.compose.rememberNavController()
      com.lastaoutdoor.lasta.ui.theme.LastaTheme {
        androidx.navigation.compose.NavHost(
          navController = navController,
          startDestination = "DiscoveryScreen"
        ) {
          composable(route = "DiscoveryScreen") { com.lastaoutdoor.lasta.ui.screen.discovery.DiscoveryScreen() }
        }
      }
    }

  }

  @Test
  fun discoveryScreen_isDisplayed() {
    composeRule.onNodeWithTag("Discovery").assertIsDisplayed()
  }

}
