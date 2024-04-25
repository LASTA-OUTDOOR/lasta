package com.lastaoutdoor.lasta.ui.screen.social

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NewMessageScreenKtTest {
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Viewmodel and repository
  private lateinit var socialViewModel: SocialViewModel

  // Navigation controller
  private lateinit var navController: NavController

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
    // set up the navigation controller
    navController = NavHostController(composeRule.activity)
  }

  // Default layout of the page
  @Test
  fun initialState() {
    // Set the content to the social screen
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      (socialViewModel.connectionRepo as FakeConnectivityRepository).setConnectionStateToFalse()
      NewMessageScreen(navController)
    }

    // Header (title)
    composeRule.onNodeWithTag("Header").assertIsDisplayed()
  }
}
