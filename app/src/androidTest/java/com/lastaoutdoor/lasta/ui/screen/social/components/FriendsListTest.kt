package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
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
          friendSuggestions = emptyList(),
          clearFriendRequestFeedback = {},
          hideAddFriendDialog = {},
          requestFriend = {},
          refreshFriends = {},
          fetchFriendsSuggestions = {},
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
          friendSuggestions = emptyList(),
          clearFriendRequestFeedback = {},
          hideAddFriendDialog = {},
          requestFriend = {},
          refreshFriends = {},
          fetchFriendsSuggestions = {},
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
          friendSuggestions = emptyList(),
          clearFriendRequestFeedback = {},
          hideAddFriendDialog = {},
          requestFriend = {},
          refreshFriends = {},
          fetchFriendsSuggestions = {},
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
          friendSuggestions = emptyList(),
          clearFriendRequestFeedback = {},
          hideAddFriendDialog = {},
          requestFriend = {},
          refreshFriends = {},
          fetchFriendsSuggestions = {},
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
          friendSuggestions = emptyList(),
          fetchFriendsSuggestions = {},
          navigateToFriendProfile = {})
    }
    composeRule.onNodeWithTag("AddFriendDialog").assertIsDisplayed()
    // click on submit button
    composeRule.onNodeWithTag("SubmitButton").performClick()
  }

  // Test climbing intermediate friend has correct text
  @Test
  fun climbingIntermediateFriendIsDisplayed() {
    composeRule.activity.setContent {
      FriendsCard(
          friend =
              UserModel(
                  "1",
                  levels =
                      UserActivitiesLevel(
                          UserLevel.INTERMEDIATE, UserLevel.INTERMEDIATE, UserLevel.INTERMEDIATE),
                  prefActivity = ActivityType.CLIMBING),
          listOf()) {}
    }
    composeRule.onNodeWithTag("FriendCard").assertIsDisplayed()
  }

  // Test hiking intermediate friend has correct text
  @Test
  fun hikingIntermediateFriendIsDisplayed() {
    composeRule.activity.setContent {
      FriendsCard(
          friend =
              UserModel(
                  "1",
                  levels =
                      UserActivitiesLevel(
                          UserLevel.INTERMEDIATE, UserLevel.INTERMEDIATE, UserLevel.INTERMEDIATE),
                  prefActivity = ActivityType.HIKING),
          listOf()) {}
    }
    composeRule.onNodeWithTag("FriendCard").assertIsDisplayed()
  }

  // Test biking intermediate friend has correct text
  @Test
  fun bikingIntermediateFriendIsDisplayed() {
    composeRule.activity.setContent {
      FriendsCard(
          friend =
              UserModel(
                  "1",
                  levels =
                      UserActivitiesLevel(
                          UserLevel.INTERMEDIATE, UserLevel.INTERMEDIATE, UserLevel.INTERMEDIATE),
                  prefActivity = ActivityType.BIKING),
          listOf()) {}
    }
    composeRule.onNodeWithTag("FriendCard").assertIsDisplayed()
  }

  // Test climbing advanced friend has correct text
  @Test
  fun climbingAdvancedFriendIsDisplayed() {
    composeRule.activity.setContent {
      FriendsCard(
          friend =
              UserModel(
                  "1",
                  levels =
                      UserActivitiesLevel(
                          UserLevel.ADVANCED, UserLevel.ADVANCED, UserLevel.ADVANCED),
                  prefActivity = ActivityType.CLIMBING),
          listOf()) {}
    }
    composeRule.onNodeWithTag("FriendCard").assertIsDisplayed()
  }

  // Test hiking advanced friend has correct text
  @Test
  fun hikingAdvancedFriendIsDisplayed() {
    composeRule.activity.setContent {
      FriendsCard(
          friend =
              UserModel(
                  "1",
                  levels =
                      UserActivitiesLevel(
                          UserLevel.ADVANCED, UserLevel.ADVANCED, UserLevel.ADVANCED),
                  prefActivity = ActivityType.HIKING),
          listOf()) {}
    }
    composeRule.onNodeWithTag("FriendCard").assertIsDisplayed()
  }

  // Test biking advanced friend has correct text
  @Test
  fun bikingAdvancedFriendIsDisplayed() {
    composeRule.activity.setContent {
      FriendsCard(
          friend =
              UserModel(
                  "1",
                  levels =
                      UserActivitiesLevel(
                          UserLevel.ADVANCED, UserLevel.ADVANCED, UserLevel.ADVANCED),
                  prefActivity = ActivityType.BIKING),
          listOf()) {}
    }
    composeRule.onNodeWithTag("FriendCard").assertIsDisplayed()
  }

  @Test
  fun testFriendsCard_with1CommonFriends() {
    val userModel = UserModel(userId = "1", friends = listOf("17", "3", "4"))
    val friendList =
        listOf(UserModel(userId = "2"), UserModel(userId = "3"), UserModel(userId = "7"))
    composeRule.activity.setContent { FriendsCard(userModel, friendList) {} }
    composeRule.onNodeWithText("1 friend in common").assertIsDisplayed()
  }

  @Test
  fun testFriendsCard_with2CommonFriends() {
    val userModel = UserModel(userId = "1", friends = listOf("2", "3", "4"))
    val friendList =
        listOf(UserModel(userId = "2"), UserModel(userId = "3"), UserModel(userId = "7"))
    composeRule.activity.setContent { FriendsCard(userModel, friendList) {} }
    composeRule.onNodeWithText("2 friends in common").assertIsDisplayed()
  }
}
