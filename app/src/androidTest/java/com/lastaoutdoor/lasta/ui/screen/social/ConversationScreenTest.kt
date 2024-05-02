package com.lastaoutdoor.lasta.ui.screen.social

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class ConversationScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
  }

  // Test that a conversation screen with null model is displayed
  @Test
  fun conversationScreenIsDisplayed() {
    composeRule.activity.setContent {
      ConversationScreen(
          conversationModel = null,
          refresh = {},
          userId = "",
          friendId = "",
          showSendDialog = {},
          navigateBack = {})
    }
    composeRule.onNodeWithTag("ConversationScreen").assertIsNotDisplayed()
  }

  // Test that a conversation screen with a conversation model is displayed
  @Test
  fun conversationScreenWithModelIsDisplayed() {
    composeRule.activity.setContent {
      ConversationScreen(
          conversationModel =
              ConversationModel(
                  members = listOf("1", "2"),
                  messages = listOf(MessageModel("1", "2", Timestamp(0, 0))),
                  lastMessage = MessageModel("2", "1", Timestamp(0, 0))),
          refresh = {},
          userId = "1",
          friendId = "2",
          showSendDialog = {},
          navigateBack = {})
    }
    composeRule.onNodeWithTag("ConversationScreen").assertIsDisplayed()
    composeRule.onNodeWithTag("ConversationScreenHeader").assertIsDisplayed()
  }
}
