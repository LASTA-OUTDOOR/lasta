package com.lastaoutdoor.lasta.ui.screen.discover

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
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

  @Before
  fun setUp() {
    hiltRule.inject()

    val selectedActivityType: StateFlow<List<ActivityType>> =
        MutableStateFlow(listOf(ActivityType.CLIMBING))

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
    composeRule.onNodeWithTag("filterScreen").assertIsDisplayed()

    composeRule.onNodeWithTag("EraseButton").performClick()

    composeRule.onNodeWithTag("applyFilterOptionsButton").performClick()
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
}
