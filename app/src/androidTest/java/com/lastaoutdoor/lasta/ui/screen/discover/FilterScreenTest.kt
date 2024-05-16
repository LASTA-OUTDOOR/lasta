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
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenCallBacks
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.MutableStateFlow
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

  private val discoverScreenCallBacks: DiscoverScreenCallBacks =
      DiscoverScreenCallBacks(
          fetchActivities = {},
          setScreen = {},
          setRange = {},
          setSelectedLocality = {},
          updatePermission = {},
          updateMarkers = { _, _ -> },
          updateSelectedMarker = {},
          clearSelectedItinerary = {},
          updateOrderingBy = {},
          clearSelectedMarker = {},
          fetchSuggestion = {},
          clearSuggestions = {},
          updateInitialPosition = {},
          updateActivities = {},
          updateRange = {},
          setSelectedActivitiesType = {},
          setSelectedLevels = {},
      )

  @Before
  fun setUp() {
    hiltRule.inject()

    val state =
        MutableStateFlow(
            DiscoverScreenState(
                isLoading = false,
                selectedLevels =
                    UserActivitiesLevel(
                        UserLevel.INTERMEDIATE, UserLevel.ADVANCED, UserLevel.ADVANCED),
                selectedActivityTypes = listOf(ActivityType.CLIMBING),
            ))

    composeRule.activity.setContent {
      intermediate = stringResource(id = R.string.filter_difficulty_level)
      beginner = stringResource(id = R.string.beginner)
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = "filterScreen") {
        composable("filterScreen") {
          FilterScreen(
              state,
              discoverScreenCallBacks,
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

    val state =
        MutableStateFlow(
            DiscoverScreenState(
                isLoading = false,
                selectedLevels =
                    UserActivitiesLevel(
                        UserLevel.INTERMEDIATE, UserLevel.ADVANCED, UserLevel.ADVANCED),
                selectedActivityTypes =
                    listOf(
                        ActivityType.CLIMBING,
                        ActivityType.HIKING,
                        ActivityType.BIKING,
                    ),
            ))

    composeRule.activity.setContent { FilterScreen(state, discoverScreenCallBacks) {} }

    composeRule.onNodeWithTag("filterScreen").assertIsDisplayed()

    val state1 =
        MutableStateFlow(
            DiscoverScreenState(
                isLoading = false,
                selectedLevels =
                    UserActivitiesLevel(
                        UserLevel.INTERMEDIATE, UserLevel.ADVANCED, UserLevel.ADVANCED),
                selectedActivityTypes = listOf(ActivityType.CLIMBING, ActivityType.HIKING),
            ))

    composeRule.activity.setContent { FilterScreen(state1, discoverScreenCallBacks) {} }

    composeRule.onNodeWithTag("filterScreen").assertIsDisplayed()
    composeRule.onNodeWithTag("filterScreen").assertIsDisplayed()

    val state2 =
        MutableStateFlow(
            DiscoverScreenState(
                isLoading = false,
                selectedLevels =
                    UserActivitiesLevel(
                        UserLevel.INTERMEDIATE, UserLevel.ADVANCED, UserLevel.ADVANCED),
                selectedActivityTypes =
                    listOf(ActivityType.CLIMBING, ActivityType.HIKING, ActivityType.BIKING),
            ))
    composeRule.activity.setContent { FilterScreen(state2, discoverScreenCallBacks) {} }

    composeRule.onNodeWithTag("filterScreen").assertIsDisplayed()
  }
}
