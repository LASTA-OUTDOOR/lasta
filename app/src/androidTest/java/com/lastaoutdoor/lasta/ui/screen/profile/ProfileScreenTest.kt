package com.lastaoutdoor.lasta.ui.screen.profile

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
class ProfileScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      val rootNavController = rememberNavController()
      ProfileScreen(rootNavController = rootNavController)
    }
  }

  @Test
  fun profileScreenIsDisplayed() {
    composeRule.onNodeWithTag("ProfileScreen").assertIsDisplayed()
  }

  @Test
  fun showDialogWorks() {
    composeRule.onNodeWithTag("showDialog").performClick()
    composeRule.onNodeWithTag("AlertDialog").assertIsDisplayed()
    composeRule.onNodeWithTag("HikingLevelItem1").performClick()
  }

  @Test
  fun testSpinner() {
    composeRule.onNodeWithTag("spinnerIcon").performClick()
    composeRule.onNodeWithTag("DropdownItem1").performClick()
    composeRule.onNodeWithTag("TestClimb").assertIsDisplayed()
  }
}
