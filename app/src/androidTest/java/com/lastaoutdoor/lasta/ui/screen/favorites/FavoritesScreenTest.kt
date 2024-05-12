package com.lastaoutdoor.lasta.ui.screen.favorites

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class FavoritesScreenTest {

  // Allow Hilt to inject dependencies
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @Test
  fun favoritesScreen_isLoading() {
    composeRule.activity.setContent {
      MaterialTheme {
        FavoritesScreen(
            isLoading = true,
            activities = emptyList(),
            centerPoint = LatLng(46.519962, 6.633597),
            favorites = emptyList(),
            changeActivityToDisplay = {},
            flipFavorite = {},
            changeWeatherTarget = {}) {}
      }
    }
    composeRule.onNodeWithTag("LoadingBarFavorites").assertIsDisplayed()
  }

  @Test
  fun favoritesScreen_isDisplayed() {
    composeRule.activity.setContent {
      MaterialTheme {
        FavoritesScreen(
            isLoading = false,
            activities =
                listOf(
                    Activity(
                        "1",
                        11033919,
                        ActivityType.HIKING,
                        "Chemin panorama alpin",
                        Position(46.4718332, 6.8338907),
                        0.0f,
                        0,
                        emptyList(),
                        difficulty = Difficulty.EASY,
                        activityImageUrl = "")),
            centerPoint = LatLng(46.519962, 6.633597),
            favorites = listOf("1"),
            changeActivityToDisplay = {},
            flipFavorite = {},
            changeWeatherTarget = {}) {}
      }
    }
    composeRule.onNodeWithTag("FavoritesScreen").assertIsDisplayed()
    composeRule.onNodeWithTag("1activityCard").assertIsDisplayed()
  }
}
