package com.lastaoutdoor.lasta.ui.screen.discovery

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.lastaoutdoor.lasta.data.api.FakeOutdoorActivityRepository
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.viewmodel.DiscoveryScreenViewModel
import dagger.hilt.android.testing.BindValue
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

  @BindValue
  val discoveryScreenViewModel: DiscoveryScreenViewModel =
      DiscoveryScreenViewModel(FakeOutdoorActivityRepository())

  @Before
  fun setUp() {
    hiltRule.inject()
    /**
     * composeRule.activity.setContent { val navController =
     * androidx.navigation.compose.rememberNavController()
     * com.lastaoutdoor.lasta.ui.theme.LastaTheme { androidx.navigation.compose.NavHost(
     * navController = navController, startDestination = "DiscoveryScreen") { composable(route =
     * "DiscoveryScreen") { DiscoveryScreen() } } } }
     */
  }

  // Test if discovery screen is displayed
  @Test
  fun discoveryScreen_isDisplayed() {
    composeRule.activity.setContent { DiscoveryScreen() }
    composeRule.onNodeWithTag("discoveryScreen").assertIsDisplayed()
  }

  // Test if discovery content is displayed
  @Test
  fun discoveryScreen_hasContent() {
    composeRule.activity.setContent { DiscoveryScreen() }
    composeRule.onNodeWithTag("discoveryContent").assertIsDisplayed()
  }

  // Test if discovery screen has list
  @Test
  fun discoveryScreen_hasList() {
    composeRule.activity.setContent { DiscoveryScreen() }
    composeRule.onNodeWithTag("outdoorActivityList").assertIsDisplayed()
  }

  @Test
  fun discoveryScreen_hasFloatingActionButton() {
    composeRule.activity.setContent { DiscoveryScreen() }
    composeRule.onNodeWithTag("floatingActionButtons").assertIsDisplayed()
  }

  // Test if discovery screen has outdoor activity item and valid buttons
  @Test
  fun discoveryScreen_hasOutdoorActivityItem_andValidButtons() {
    discoveryScreenViewModel.climbingActivities =
        mutableListOf(OutdoorActivity(ActivityType.CLIMBING, 1, 1.0f, "1 hour", "Climbing"))
    composeRule.activity.setContent {
      OutdoorActivityList(outdoorActivities = discoveryScreenViewModel.climbingActivities)
    }
    composeRule.onNodeWithTag("outdoorActivityItem").assertIsDisplayed()
  }

  // Test if activity dialog shows
  @Test
  fun activityDialog_isDisplayed() {
    discoveryScreenViewModel.climbingActivities =
        mutableListOf(OutdoorActivity(ActivityType.CLIMBING, 1, 1.0f, "1 hour", "Climbing"))
    composeRule.activity.setContent {
      ActivityDialog(
          onDismissRequest = { /*dismiss dialog on clicking "Ok"*/
            discoveryScreenViewModel.displayDialog.value = false
          },
          outdoorActivity = discoveryScreenViewModel.activityToDisplay.value)
    }
    composeRule.onNodeWithTag("activityDialog").assertIsDisplayed()
  }
}
