package com.lastaoutdoor.lasta.ui.screen.moreinfo

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.ui.screen.activities.MoreInfoScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class MoreInfoScreenTest {
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

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
  fun startButton_isDisplayed(){
    composeRule.onNodeWithTag("Start button").assertIsDisplayed()
  }
}
