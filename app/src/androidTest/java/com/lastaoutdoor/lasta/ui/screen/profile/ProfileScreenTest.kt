package com.lastaoutdoor.lasta.ui.screen.profile

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.utils.TimeFrame
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class ProfileScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  private lateinit var userModel: UserModel

  @Before
  fun setUp() {
    userModel = UserModel("")
    hiltRule.inject()
    composeRule.activity.setContent {
      ProfileScreen(listOf(), TimeFrame.M, ActivityType.CLIMBING, true, userModel, {}, {}, {}, {})
    }
  }

  @Test
  fun profileScreenIsDisplayed() {
    composeRule.onNodeWithTag("ProfileScreen").assertIsDisplayed()
  }

  @Test
  fun testSpinner() {
    composeRule.onNodeWithTag("spinnerIcon").performClick()
    composeRule.onNodeWithTag("DropdownItem1").performClick()
    composeRule.onNodeWithTag("TestClimb").assertIsDisplayed()
  }
}
