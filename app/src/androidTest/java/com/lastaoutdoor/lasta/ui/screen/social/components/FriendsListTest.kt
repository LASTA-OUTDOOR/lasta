package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.utils.ConnectionState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FriendsListTest {

  // Allow Hilt to inject dependencies
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
  }

  // Test that a friend list instanced with a friend is displayed
  @Test
  fun friendsListWithFriendsIsDisplayed() {
    composeRule.activity.setContent {
      FriendsList(
          isConnected = ConnectionState.CONNECTED,
          friends =
              listOf(
                  UserModel("1"),
              ),
          displayAddFriendDialog = false,
          friendRequestFeedback = "",
          clearFriendRequestFeedback = {},
          hideAddFriendDialog = {},
          requestFriend = {},
          refreshFriends = {},
          navigateToFriendProfile = {})
    }
    composeRule.onNodeWithTag("FriendCard").assertIsDisplayed()
  }

  // Test that a friend list instanced with a biking friend is displayed
  @Test
  fun friendsListWithBikingFriendIsDisplayed() {
    composeRule.activity.setContent {
      FriendsList(
          isConnected = ConnectionState.CONNECTED,
          friends =
              listOf(
                  UserModel("1", prefActivity = ActivityType.BIKING),
              ),
          displayAddFriendDialog = false,
          friendRequestFeedback = "",
          clearFriendRequestFeedback = {},
          hideAddFriendDialog = {},
          requestFriend = {},
          refreshFriends = {},
          navigateToFriendProfile = {})
    }
    composeRule.onNodeWithTag("FriendCard").assertIsDisplayed()
  }

  // Test that instancing a friend list with no friends displays a message
  @Test
  fun friendsListWithNoFriendsIsDisplayed() {
    composeRule.activity.setContent {
      FriendsList(
          isConnected = ConnectionState.CONNECTED,
          friends = emptyList(),
          displayAddFriendDialog = true,
          friendRequestFeedback = "",
          clearFriendRequestFeedback = {},
          hideAddFriendDialog = {},
          requestFriend = {},
          refreshFriends = {},
          navigateToFriendProfile = {})
    }
    composeRule.onNodeWithTag("AddFriendDialog").assertIsDisplayed()
  }

  // Test that instancing a friend list offline works correctly
  @Test
  fun friendsListOfflineIsDisplayed() {
    composeRule.activity.setContent {
      FriendsList(
          isConnected = ConnectionState.OFFLINE,
          friends = emptyList(),
          displayAddFriendDialog = false,
          friendRequestFeedback = "",
          clearFriendRequestFeedback = {},
          hideAddFriendDialog = {},
          requestFriend = {},
          refreshFriends = {},
          navigateToFriendProfile = {})
    }
    composeRule.onNodeWithTag("ConnectionMissing").assertIsDisplayed()
  }

  // Test that instancing a friend list with a hiking friend and a display add friend dialog works
  // correctly
  @Test
  fun friendsListWithHikingFriendAndAddFriendDialogIsDisplayed() {
    composeRule.activity.setContent {
      FriendsList(
          isConnected = ConnectionState.CONNECTED,
          friends =
              listOf(
                  UserModel("1", prefActivity = ActivityType.HIKING),
              ),
          displayAddFriendDialog = true,
          friendRequestFeedback = "",
          clearFriendRequestFeedback = {},
          hideAddFriendDialog = {},
          requestFriend = {},
          refreshFriends = {},
          navigateToFriendProfile = {})
    }
    composeRule.onNodeWithTag("AddFriendDialog").assertIsDisplayed()
  }
}
