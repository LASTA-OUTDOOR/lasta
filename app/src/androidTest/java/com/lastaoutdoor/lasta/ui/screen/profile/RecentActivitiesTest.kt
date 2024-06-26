package com.lastaoutdoor.lasta.ui.screen.profile

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.user.BikingUserActivity
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.HikingUserActivity
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class RecentActivitiesTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @Test
  fun recentActivitiesDisplaysTrails() {
    val listOfActivities: List<UserActivity> = listOf(BikingUserActivity())
    composeRule.activity.setContent { RecentActivities(listOfActivities) }
    composeRule.onNodeWithTag("RecentActivitiesItem").assertIsDisplayed()
    composeRule.activity.setContent { RecentActivities(listOf(HikingUserActivity())) }
    composeRule.onNodeWithTag("RecentActivitiesItem").assertIsDisplayed()
    composeRule.activity.setContent { RecentActivities(listOf(ClimbingUserActivity())) }
    composeRule.onNodeWithTag("RecentActivitiesItem").assertIsDisplayed()
  }
}
