package com.lastaoutdoor.lasta.ui.screen.discover

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
@UninstallModules(AppModule::class)
class DiscoveryScreenTest {

  // Allow Hilt to inject dependencies
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
  }

  // Test if discovery screen is displayed
  /*@Test
  fun discoveryScreen_isDisplayed() {
    composeRule.activity.setContent {
      val navController = rememberNavController()
      DiscoveryScreen(navController, moreInfoScreenViewModel = hiltViewModel())
    }
    composeRule.onNodeWithTag("discoveryScreen").assertIsDisplayed()
  }

  // Test if modal bottom sheet is displayed on list mode correctly when isRangePopup is true
  @Test
  fun modalBottomSheet_isDisplayed() {
    composeRule.activity.setContent {
      RangeSearchComposable(
          discoveryScreenViewModel,
          isRangePopup = true,
          onDismissRequest = { /*dismiss dialog on clicking "Ok"*/})
    }
    composeRule.onNodeWithTag("searchOptions").assertIsDisplayed()
    // slide the slider to the right
    composeRule
        .onNodeWithTag("listSearchOptionsSlider")
        .performTouchInput(block = { ViewActions.swipeRight() })
    // change the value of the slider
    composeRule.onNodeWithTag("listSearchOptionsSlider").performClick()
    // click on the apply button
    composeRule.onNodeWithTag("listSearchOptionsApplyButton").performClick()
  }

  // Test if modal bottom sheet is displayed on map mode correctly when isRangePopup is true
  @Test
  fun modalBottomSheet_isDisplayed_mapMode() {
    composeRule.activity.setContent {
      discoveryScreenViewModel.setScreen(DiscoveryScreenType.MAP)
      RangeSearchComposable(
          discoveryScreenViewModel,
          isRangePopup = true,
          onDismissRequest = { /*dismiss dialog on clicking "Ok"*/})
    }
    composeRule.onNodeWithTag("rangeSearch").assertIsDisplayed()
    // slide the slider to the right
    composeRule
        .onNodeWithTag("mapRangeSearchSlider")
        .performTouchInput(block = { ViewActions.swipeRight() })
    // change the value of the slider
    composeRule.onNodeWithTag("mapRangeSearchSlider").performClick()
    // click on the apply button
    composeRule.onNodeWithTag("mapRangeSearchApplyButton").performClick()
  }

  // Test the locality selection dropdown
  @Test
  fun localityDropdown_isDisplayed() {
    composeRule.activity.setContent {
      DiscoveryScreen(
          navController = rememberNavController(),
          discoveryScreenViewModel,
          moreInfoScreenViewModel = hiltViewModel())
    }
    composeRule.onNodeWithTag("listSearchOptionsEnableButton").performClick()
    composeRule.onNodeWithTag("localitySelectionDropdown").assertIsDisplayed()
    // click on the locality selection dropdown
    composeRule.onNodeWithTag("localitySelectionDropdownButton").performClick()
  }

  // Test the modal upper sheet
  @Test
  fun modalUpperSheet_isDisplayed() {
    composeRule.activity.setContent {
      DiscoveryScreen(
          navController = rememberNavController(),
          discoveryScreenViewModel,
          moreInfoScreenViewModel = hiltViewModel())
    }
    composeRule.onNodeWithTag("listSearchOptionsEnableButton").performClick()
    composeRule.onNodeWithTag("modalUpperSheet").assertIsDisplayed()
  }*/

  /*
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

    // Test if discovery screen has floating action button
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
      composeRule.onNodeWithTag("moreInfoButton").performClick()
      composeRule.onNodeWithTag("mapButton").performClick()
      composeRule.onNodeWithTag("startButton").performClick()
      composeRule.onNodeWithTag("outdoorActivityItem").assertExists()
      composeRule.onNodeWithTag("outdoorActivityItem").assertIsDisplayed()
    }

    // Test if activity dialog with valid info shows
    @Test
    fun activityDialog_isDisplayed() {
      val act = OutdoorActivity(ActivityType.CLIMBING, 1, 1.0f, "1 hour", "Zurich")
      composeRule.activity.setContent {
        ActivityDialog(
            onDismissRequest = { /*dismiss dialog on clicking "Ok"*/
              discoveryScreenViewModel.displayDialog.value = false
            },
            outdoorActivity = act)
      }
      composeRule.onNodeWithTag("okButton").performClick()
      composeRule.onNodeWithTag("locationText").assertTextContains("Location: Zurich")
      composeRule.onNodeWithTag("durationText").assertTextContains("Duration: 1 hour")
      composeRule.onNodeWithTag("difficultyText").assertTextContains("Difficulty: 1")
      composeRule.onNodeWithTag("activityDialog").assertIsDisplayed()
    }

    // Test if activity dialog with empty info shows
    @Test
    fun activityDialog_isDisplayed_emptyInfo() {
      val act = OutdoorActivity(ActivityType.CLIMBING, 0, 1.0f, "", "")
      composeRule.activity.setContent {
        ActivityDialog(
            onDismissRequest = { /*dismiss dialog on clicking "Ok"*/
              discoveryScreenViewModel.displayDialog.value = false
            },
            outdoorActivity = act)
      }
      composeRule.onNodeWithTag("locationText").assertTextContains("No available location")
      composeRule.onNodeWithTag("durationText").assertTextContains("No available duration")
      composeRule.onNodeWithTag("difficultyText").assertTextContains("No available difficulty")
      composeRule.onNodeWithTag("activityDialog").assertIsDisplayed()
    }

    // Test that setting dialog.value to true calls ActivityDialog
    @Test
    fun showDialog_isCalled() {
      discoveryScreenViewModel.displayDialog.value = true
      composeRule.activity.setContent { DiscoveryContent() }
      composeRule.onNodeWithTag("activityDialog").assertIsDisplayed()
    }
  }
  */
}
