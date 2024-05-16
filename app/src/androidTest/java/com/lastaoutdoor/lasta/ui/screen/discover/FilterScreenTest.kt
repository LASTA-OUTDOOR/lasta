package com.lastaoutdoor.lasta.ui.screen.discover

import androidx.activity.compose.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.ui.screen.loading.LoadingScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class FilterScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  private lateinit var intermediate: String
  private lateinit var beginner: String

  private val mockUserActivitiesLevel =
      UserActivitiesLevel(
          climbingLevel = UserLevel.BEGINNER,
          hikingLevel = UserLevel.INTERMEDIATE,
          bikingLevel = UserLevel.ADVANCED)

  private val mockSelectedLevels = MutableStateFlow(mockUserActivitiesLevel)
  private val mockSelectedActivitiesType = MutableStateFlow(listOf<ActivityType>())
  private var navigateBackTriggered = false
  private var updatedLevels = false
  private var updatedActivities = false

  @Before
  fun setUp() {
    hiltRule.inject()

    val selectedActivityType: StateFlow<List<ActivityType>> =
        MutableStateFlow(listOf(ActivityType.CLIMBING))

    val selectedLevels =
        MutableStateFlow(
            UserActivitiesLevel(UserLevel.INTERMEDIATE, UserLevel.ADVANCED, UserLevel.ADVANCED))

    composeRule.activity.setContent {
      intermediate = stringResource(id = R.string.filter_difficulty_level)
      beginner = stringResource(id = R.string.beginner)
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = "filterScreen") {
        composable("filterScreen") {
          FilterScreen(
              selectedActivitiesType = selectedActivityType,
              setSelectedActivitiesType = {},
              selectedLevels = selectedLevels,
              setSelectedLevels = {},
          ) {
            // go to loading
            navController.navigate("loading")
          }
        }
        composable("loading") { LoadingScreen(null, {}, {}) }
      }
    }
  }

  @Test
  fun filterScreen_isDisplayed() {
    composeRule.onNodeWithTag("filterScreen").assertIsDisplayed()
  }

  @Test
  fun buttons_areWorking() {
    composeRule.onNodeWithTag("ToggleButtonClimbing").performClick()
    composeRule.onNodeWithTag("ToggleButtonHiking").performClick()
    composeRule.onNodeWithTag("ToggleButtonBiking").performClick()
    composeRule.onNodeWithTag("ToggleButtonClimbing").performClick()
    composeRule.onNodeWithTag("ToggleButtonHiking").performClick()

    composeRule.onNodeWithTag("difficultyLevelButton0").performClick()
    composeRule.onNodeWithTag("DropdownItem0").performClick()
    composeRule.onNodeWithTag("applyFilterOptionsButton").performClick()
  }

  @Test
  fun eraseButton_isWorking() {
    composeRule.onNodeWithTag("ToggleButtonClimbing").performClick()
    composeRule.onNodeWithTag("ToggleButtonHiking").performClick()
    composeRule.onNodeWithTag("ToggleButtonClimbing").performClick()

    composeRule.onNodeWithTag("difficultyLevelButton0").performClick()
    composeRule.onNodeWithTag("DropdownItem0").performClick()

    composeRule.onNodeWithTag("EraseButton").performClick()

    composeRule.onNodeWithText(intermediate).assertIsDisplayed()
  }

  @Test
  fun variousNrOfActivities_areWorking() {
    val selectedActivityType: StateFlow<List<ActivityType>> = MutableStateFlow(listOf())

    val selectedLevels =
        MutableStateFlow(
            UserActivitiesLevel(UserLevel.INTERMEDIATE, UserLevel.ADVANCED, UserLevel.ADVANCED))

    composeRule.activity.setContent {
      FilterScreen(
          selectedActivitiesType = selectedActivityType,
          setSelectedActivitiesType = {},
          selectedLevels = selectedLevels,
          setSelectedLevels = {},
      ) {}
    }
    composeRule.onNodeWithTag("filterScreen").assertIsDisplayed()
    val selectedActivityType1: StateFlow<List<ActivityType>> =
        MutableStateFlow(listOf(ActivityType.CLIMBING, ActivityType.HIKING))

    val selectedLevels1 =
        MutableStateFlow(
            UserActivitiesLevel(UserLevel.INTERMEDIATE, UserLevel.ADVANCED, UserLevel.ADVANCED))

    composeRule.activity.setContent {
      FilterScreen(
          selectedActivitiesType = selectedActivityType1,
          setSelectedActivitiesType = {},
          selectedLevels = selectedLevels1,
          setSelectedLevels = {},
      ) {}
    }
    composeRule.onNodeWithTag("filterScreen").assertIsDisplayed()
    composeRule.onNodeWithTag("filterScreen").assertIsDisplayed()
    val selectedActivityType2: StateFlow<List<ActivityType>> =
        MutableStateFlow(listOf(ActivityType.CLIMBING, ActivityType.HIKING, ActivityType.BIKING))

    val selectedLevels2 =
        MutableStateFlow(
            UserActivitiesLevel(UserLevel.INTERMEDIATE, UserLevel.ADVANCED, UserLevel.ADVANCED))

    composeRule.activity.setContent {
      FilterScreen(
          selectedActivitiesType = selectedActivityType2,
          setSelectedActivitiesType = {},
          selectedLevels = selectedLevels2,
          setSelectedLevels = {},
      ) {}
    }
    composeRule.onNodeWithTag("filterScreen").assertIsDisplayed()
  }

  @Test
  fun test_EraseButton() {
    var updatedLevels: UserActivitiesLevel? = null
    var updatedActivities: List<ActivityType>? = null

    composeRule.activity.setContent {
      FilterScreen(
          selectedLevels = mockSelectedLevels,
          setSelectedLevels = { updatedLevels = it },
          selectedActivitiesType = mockSelectedActivitiesType,
          setSelectedActivitiesType = { updatedActivities = it },
          navigateBack = {})
    }

    composeRule.onNodeWithTag("EraseButton").performClick()

    assertEquals(updatedLevels, null)
    assertEquals(updatedActivities?.isEmpty(), null)
  }

  @Test
  fun test_ApplyFilterOptionsButton() {
    updatedLevels = false
    updatedActivities = false
    navigateBackTriggered = false

    composeRule.activity.setContent {
      FilterScreen(
          selectedLevels = mockSelectedLevels,
          setSelectedLevels = { updatedLevels = true },
          selectedActivitiesType = mockSelectedActivitiesType,
          setSelectedActivitiesType = { updatedActivities = true },
          navigateBack = { navigateBackTriggered = true })
    }

    composeRule.onNodeWithTag("applyFilterOptionsButton").performClick()

    // Check if the values are updated
    assertEquals(true, updatedActivities)
    assertEquals(navigateBackTriggered, true)
  }
}
