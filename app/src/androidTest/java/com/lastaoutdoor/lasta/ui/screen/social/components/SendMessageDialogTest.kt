package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class SendMessageDialogTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
  }

  // Test that a send message dialog is displayed
  @Test
  fun sendMessageDialogIsDisplayed() {
    composeRule.activity.setContent { SendMessageDialog(hideDialog = {}, send = {}) }
    composeRule.onNodeWithTag("SendMessageDialog").assertIsDisplayed()
    composeRule.onNodeWithTag("SendMessageForm").assertIsDisplayed()
    composeRule.onNodeWithTag("SendMessageButton").performClick()
  }
}
