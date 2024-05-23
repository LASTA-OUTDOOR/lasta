package com.lastaoutdoor.lasta.ui.screen.social

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.UserModel
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
          changeActivityToDisplay = {},
          user = UserModel("1"),
          friend = UserModel("2"),
          send = {},
          navigateBack = {},
          navigateToMoreInfo = {})
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
                  members = listOf(UserModel("1"), UserModel("2")),
                  messages = listOf(MessageModel(UserModel("1"), "2", Timestamp(0, 0))),
                  lastMessage = MessageModel(UserModel("2"), "1", Timestamp(0, 0))),
          refresh = {},
          changeActivityToDisplay = {},
          user = UserModel("1"),
          friend = UserModel("2"),
          send = {},
          navigateBack = {},
          navigateToMoreInfo = {})
    }
    composeRule.onNodeWithTag("ConversationScreen").assertIsDisplayed()
    composeRule.onNodeWithTag("ConversationScreenHeader").assertIsDisplayed()
    composeRule.onNodeWithTag("ConversationScreenHeader").assertIsDisplayed()
    composeRule.onNodeWithTag("SendMessageButton").assertIsDisplayed()
    composeRule.onNodeWithTag("MessageTextField").assertIsDisplayed()
  }

  // Test that the activities shared are properly displayed
  @Test
  fun activitiesSharedAreDisplayed() {
    composeRule.activity.setContent {
      ConversationScreen(
          conversationModel =
              ConversationModel(
                  members = listOf(UserModel("1"), UserModel("2")),
                  messages =
                      listOf(
                          MessageModel(
                              UserModel("1"),
                              "|$@!|QMY0WMqi54uSvnRuzR5u|Terasses de Lavaux|CLIMBING|HARD",
                              Timestamp(0, 0)),
                          MessageModel(
                              UserModel("2"),
                              "|$@!|QMY0WMqi54uSvnRuzR5v|Terasses de Lavaux|BIKING|NORMAL",
                              Timestamp(0, 0)),
                          MessageModel(
                              UserModel("2"),
                              "|$@!|QMY0WMqi54uSvnRuzR5x|Terasses de Lavaux|HIKING|EASY",
                              Timestamp(0, 0))),
                  lastMessage =
                      MessageModel(
                          UserModel("2"),
                          "|$@!|QMY0WMqi54uSvnRuzR5x|Terasses de Lavaux|HIKING|EASY",
                          Timestamp(0, 0))),
          refresh = {},
          changeActivityToDisplay = {},
          user = UserModel("1"),
          friend = UserModel("2"),
          send = {},
          navigateBack = {},
          navigateToMoreInfo = {})
    }
    composeRule.onNodeWithTag("ActivitySharedQMY0WMqi54uSvnRuzR5u").assertIsDisplayed()
    composeRule.onNodeWithTag("ActivitySharedQMY0WMqi54uSvnRuzR5v").assertIsDisplayed()
    composeRule.onNodeWithTag("ActivitySharedQMY0WMqi54uSvnRuzR5x").assertIsDisplayed()
    composeRule.onNodeWithTag("ActivitySharedQMY0WMqi54uSvnRuzR5u").performClick()
  }

  @Test
  fun sendButtonTest() {

    var clicked = false

    composeRule.activity.setContent {
      ConversationScreen(
          conversationModel =
              ConversationModel(
                  members = listOf(UserModel("1"), UserModel("2")),
                  messages = listOf(MessageModel(UserModel("1"), "2", Timestamp(0, 0))),
                  lastMessage = MessageModel(UserModel("2"), "1", Timestamp(0, 0))),
          changeActivityToDisplay = {},
          refresh = {},
          user = UserModel("1"),
          friend = UserModel("2"),
          send = { clicked = true },
          navigateBack = {},
          navigateToMoreInfo = {})
    }
    composeRule.onNodeWithTag("SendMessageButton").assertIsDisplayed()
    composeRule.onNodeWithTag("SendMessageButton").performClick()
    assert(clicked)
  }
}
