package com.lastaoutdoor.lasta.ui.screen.discover

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
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

    var selectedActivityType: StateFlow<ActivityType> = MutableStateFlow(ActivityType.CLIMBING)
    var selectedLevels =
        MutableStateFlow(
            UserActivitiesLevel(UserLevel.INTERMEDIATE, UserLevel.ADVANCED, UserLevel.ADVANCED))
    composeRule.activity.setContent {
      FilterScreen(
          selectedActivityType = selectedActivityType,
          setSelectedActivityType = {},
          selectedLevels = selectedLevels,
          setSelectedLevels = {},
      ) {}
    }
  }

  @Test
  fun filterScreen_isDisplayed() {
    composeRule.onNodeWithTag("filterScreen").assertIsDisplayed()
  }
}
