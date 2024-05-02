package com.lastaoutdoor.lasta.ui.screen.profile.components

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.utils.DaysInWeek
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class BarGraphTest {
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      BarGraph(
          graphBarData = listOf(4f),
          xAxisScaleData = listOf(DaysInWeek.Fri),
          barData = listOf(1),
          height = 300.dp,
          roundType = BarType.CIRCULAR_TYPE,
          barWidth = 300.dp,
          barColor = Color.Gray,
          barArrangement = Arrangement.SpaceEvenly)
    }
  }

  @Test
  fun barGraph_isDisplayed() {
    composeRule.onNodeWithTag("BarBox").assertIsDisplayed()
    composeRule.onNodeWithTag("BarRow").assertIsDisplayed()
    composeRule.onNodeWithTag("BarCol").assertIsDisplayed()
  }
}
