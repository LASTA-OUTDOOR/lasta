package com.lastaoutdoor.lasta.ui.screen.social

import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.FriendsActivities
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.ui.screen.social.components.MessageMissing
import com.lastaoutdoor.lasta.utils.ConnectionState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class SocialTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
  }

  // Default layout of the page
  @Test
  fun initialState() {
    val showTopButton: (ImageVector, () -> Unit) -> Unit = { _, _ ->
      // This is a fake function, so we do nothing here.
    }
    val topButtonIcon = mutableStateOf(Icons.Filled.Email)
    // Set the content to the social screen
    composeRule.activity.setContent {
      SocialScreen(
          hasFriendRequests = true,
          topButton = true,
          topButtonIcon = topButtonIcon.value,
          topButtonOnClick = { /*TODO*/},
          refreshFriendRequests = { /*TODO*/},
          navigateToNotifications = { /*TODO*/},
          isConnected = ConnectionState.CONNECTED,
          friends =
              List(1) {
                UserModel(
                    "1",
                    "John",
                    "Doe",
                    "",
                )
              },
          messages = emptyList(),
          latestFriendActivities = emptyList(),
          addFriendDialog = true,
          friendRequestFeedback = "Hi!",
          isDisplayedFriendPicker = true,
          refreshMessages = { /*TODO*/},
          clearFriendRequestFeedback = { /*TODO*/},
          hideAddFriendDialog = { /*TODO*/},
          requestFriend = { /*TODO*/},
          refreshFriends = { /*TODO*/},
          showTopButton = showTopButton,
          hideTopButton = { /*TODO*/},
          displayAddFriendDialog = { /*TODO*/},
          displayFriendPicker = { /*TODO*/},
          hideFriendPicker = { /*TODO*/},
          changeDisplayFriendPicker = { /*TODO*/},
          navigateToConversation = { /*TODO*/},
          navigateToFriendProfile = { /*TODO*/},
          refreshFriendsActivities = { /*TODO*/},
      )
    }
    composeRule.onNodeWithTag("SocialScreen").assertIsDisplayed()
    // Header (title)
    composeRule.onNodeWithTag("SocialScreenHeader").assertIsDisplayed()
    composeRule.onNodeWithTag("TabNumber1").performClick()
  }

  @Test
  fun go_to_messages_tab() {
    val showTopButton: (ImageVector, () -> Unit) -> Unit = { _, _ ->
      // This is a fake function, so we do nothing here.
    }
    val topButtonIcon = mutableStateOf(Icons.Filled.Email)
    // Set the content to the social screen
    composeRule.activity.setContent {
      SocialScreen(
          hasFriendRequests = true,
          topButton = true,
          topButtonIcon = topButtonIcon.value,
          topButtonOnClick = { /*TODO*/},
          refreshFriendRequests = { /*TODO*/},
          navigateToNotifications = { /*TODO*/},
          isConnected = ConnectionState.CONNECTED,
          friends =
              List(1) {
                UserModel(
                    "1",
                    "John",
                    "Doe",
                    "",
                )
              },
          messages = emptyList(),
          latestFriendActivities = emptyList(),
          addFriendDialog = true,
          friendRequestFeedback = "Hi!",
          isDisplayedFriendPicker = true,
          refreshMessages = { /*TODO*/},
          clearFriendRequestFeedback = { /*TODO*/},
          hideAddFriendDialog = { /*TODO*/},
          requestFriend = { /*TODO*/},
          refreshFriends = { /*TODO*/},
          showTopButton = showTopButton,
          hideTopButton = { /*TODO*/},
          displayAddFriendDialog = { /*TODO*/},
          displayFriendPicker = { /*TODO*/},
          hideFriendPicker = { /*TODO*/},
          changeDisplayFriendPicker = { /*TODO*/},
          navigateToConversation = { /*TODO*/},
          navigateToFriendProfile = { /*TODO*/},
          refreshFriendsActivities = { /*TODO*/},
      )
    }
    composeRule.onNodeWithTag("SocialScreen").assertIsDisplayed()
    // Header (title)
    composeRule.onNodeWithTag("SocialScreenHeader").assertIsDisplayed()
    // go to messages tab
    composeRule.onNodeWithTag("TabNumber1").performClick()
    composeRule.onNodeWithTag("TabNumber2").performClick()
    composeRule.onNodeWithTag("TabNumber0").performClick()
  }

  @Test
  fun message_missing_works() {
    composeRule.activity.setContent {
      MessageMissing {
        // This is a fake function, so we do nothing here.
      }
    }
    composeRule.onNodeWithTag("MessageMissing").assertIsDisplayed()
  }

  @Test
  fun message_tab_works() {
    val showTopButton: (ImageVector, () -> Unit) -> Unit = { _, _ ->
      // This is a fake function, so we do nothing here.
    }
    val topButtonIcon = mutableStateOf(Icons.Filled.Email)
    // Set the content to the social screen
    composeRule.activity.setContent {
      SocialScreen(
          hasFriendRequests = true,
          topButton = true,
          topButtonIcon = topButtonIcon.value,
          topButtonOnClick = { /*TODO*/},
          refreshFriendRequests = { /*TODO*/},
          navigateToNotifications = { /*TODO*/},
          isConnected = ConnectionState.CONNECTED,
          friends =
              List(1) {
                UserModel(
                    "1",
                    "John",
                    "Doe",
                    "",
                )
              },
          messages =
              listOf(
                  ConversationModel(
                      listOf(UserModel("1"), UserModel("2")),
                      listOf(),
                      MessageModel(UserModel("1"), "Hello Cassio", Timestamp(0, 0)))),
          latestFriendActivities = emptyList(),
          addFriendDialog = true,
          friendRequestFeedback = "Hi!",
          isDisplayedFriendPicker = true,
          refreshMessages = { /*TODO*/},
          clearFriendRequestFeedback = { /*TODO*/},
          hideAddFriendDialog = { /*TODO*/},
          requestFriend = { /*TODO*/},
          refreshFriends = { /*TODO*/},
          showTopButton = showTopButton,
          hideTopButton = { /*TODO*/},
          displayAddFriendDialog = { /*TODO*/},
          displayFriendPicker = { /*TODO*/},
          hideFriendPicker = { /*TODO*/},
          changeDisplayFriendPicker = { /*TODO*/},
          navigateToConversation = { /*TODO*/},
          navigateToFriendProfile = { /*TODO*/},
          refreshFriendsActivities = { /*TODO*/},
      )
    }
    composeRule.onNodeWithTag("SocialScreen").assertIsDisplayed()
    // Header (title)
    composeRule.onNodeWithTag("SocialScreenHeader").assertIsDisplayed()
    // go to messages tab
    composeRule.onNodeWithTag("TabNumber2").performClick()
    composeRule.onNodeWithTag("MessageCard").assertIsDisplayed()
  }
  // test message list offline
  @Test
  fun message_list_offline_works() {
    val showTopButton: (ImageVector, () -> Unit) -> Unit = { _, _ ->
      // This is a fake function, so we do nothing here.
    }
    val topButtonIcon = mutableStateOf(Icons.Filled.Email)
    // Set the content to the social screen
    composeRule.activity.setContent {
      SocialScreen(
          hasFriendRequests = true,
          topButton = true,
          topButtonIcon = topButtonIcon.value,
          topButtonOnClick = { /*TODO*/},
          refreshFriendRequests = { /*TODO*/},
          navigateToNotifications = { /*TODO*/},
          isConnected = ConnectionState.OFFLINE,
          friends =
              List(1) {
                UserModel(
                    "1",
                    "John",
                    "Doe",
                    "",
                )
              },
          messages =
              listOf(
                  ConversationModel(
                      listOf(UserModel("1"), UserModel("2")),
                      listOf(),
                      MessageModel(UserModel("1"), "Hello Cassio", Timestamp(0, 0)))),
          latestFriendActivities = emptyList(),
          addFriendDialog = true,
          friendRequestFeedback = "Hi!",
          isDisplayedFriendPicker = true,
          refreshMessages = { /*TODO*/},
          clearFriendRequestFeedback = { /*TODO*/},
          hideAddFriendDialog = { /*TODO*/},
          requestFriend = { /*TODO*/},
          refreshFriends = { /*TODO*/},
          showTopButton = showTopButton,
          hideTopButton = { /*TODO*/},
          displayAddFriendDialog = { /*TODO*/},
          displayFriendPicker = { /*TODO*/},
          hideFriendPicker = { /*TODO*/},
          changeDisplayFriendPicker = { /*TODO*/},
          navigateToConversation = { /*TODO*/},
          navigateToFriendProfile = { /*TODO*/},
          refreshFriendsActivities = { /*TODO*/},
      )
    }
    composeRule.onNodeWithTag("SocialScreen").assertIsDisplayed()
    // Header (title)
    composeRule.onNodeWithTag("SocialScreenHeader").assertIsDisplayed()
    // go to messages tab
    composeRule.onNodeWithTag("TabNumber2").performClick()
    composeRule.onNodeWithTag("ConnectionMissing").assertIsDisplayed()
  }

  // test friend with activity list
  @Test
  fun friend_with_activity_list_works() {
    val showTopButton: (ImageVector, () -> Unit) -> Unit = { _, _ ->
      // This is a fake function, so we do nothing here.
    }
    val topButtonIcon = mutableStateOf(Icons.Filled.Email)
    // Set the content to the social screen
    composeRule.activity.setContent {
      SocialScreen(
          hasFriendRequests = true,
          topButton = true,
          topButtonIcon = topButtonIcon.value,
          topButtonOnClick = { /*TODO*/},
          refreshFriendRequests = { /*TODO*/},
          navigateToNotifications = { /*TODO*/},
          isConnected = ConnectionState.CONNECTED,
          friends =
              List(1) {
                UserModel(
                    "1",
                    "John",
                    "Doe",
                    "",
                )
              },
          messages = emptyList(),
          latestFriendActivities =
              listOf(
                  FriendsActivities(UserModel("1"), ClimbingUserActivity("1"), Activity("1", 3))),
          addFriendDialog = true,
          friendRequestFeedback = "Hi!",
          isDisplayedFriendPicker = true,
          refreshMessages = { /*TODO*/},
          clearFriendRequestFeedback = { /*TODO*/},
          hideAddFriendDialog = { /*TODO*/},
          requestFriend = { /*TODO*/},
          refreshFriends = { /*TODO*/},
          showTopButton = showTopButton,
          hideTopButton = { /*TODO*/},
          displayAddFriendDialog = { /*TODO*/},
          displayFriendPicker = { /*TODO*/},
          hideFriendPicker = { /*TODO*/},
          changeDisplayFriendPicker = { /*TODO*/},
          navigateToConversation = { /*TODO*/},
          navigateToFriendProfile = { /*TODO*/},
          refreshFriendsActivities = { /*TODO*/},
      )
    }
    composeRule.onNodeWithTag("SocialScreen").assertIsDisplayed()
    // Header (title)
    composeRule.onNodeWithTag("SocialScreenHeader").assertIsDisplayed()
    // go to messages tab
    composeRule.onNodeWithTag("TabNumber0").performClick()
    composeRule.onNodeWithTag("FriendActivityCard1").assertIsDisplayed()
  }
}
