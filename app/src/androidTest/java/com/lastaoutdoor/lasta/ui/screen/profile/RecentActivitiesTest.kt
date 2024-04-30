package com.lastaoutdoor.lasta.ui.screen.profile

import com.lastaoutdoor.lasta.di.AppModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

@HiltAndroidTest
@UninstallModules(AppModule::class)
class RecentActivitiesTest {

  /*@get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @Test
  fun recentActivitiesDisplaysTrails() {
    val listOfActivities: List<ActivitiesDatabaseType> = listOf(ActivitiesDatabaseType.Trail())

    composeRule.activity.setContent { RecentActivities(listOfActivities) }
    composeRule.onNodeWithTag("RecentActivitiesItem").assertIsDisplayed()
  }*/
}
