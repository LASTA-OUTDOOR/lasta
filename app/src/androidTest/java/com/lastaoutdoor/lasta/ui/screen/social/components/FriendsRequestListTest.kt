package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.utils.ConnectionState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class FriendsRequestListTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
  }

  // Test that a friend request list instanced with a friend is displayed
  @Test
  fun friendsRequestListWithFriendsIsDisplayed() {
    composeRule.activity.setContent {
      FriendsRequestList(
          isConnected = ConnectionState.CONNECTED,
          friendRequests =
              listOf(
                  UserModel("1"),
              ),
          acceptFriend = {}) {}
    }
    composeRule.onNodeWithTag("FriendRequest").assertIsDisplayed()
  }

  // Test that a friend request list instanced with no friend is displayed
  @Test
  fun friendsRequestListWithNoFriendsIsDisplayed() {
    composeRule.activity.setContent {
      FriendsRequestList(
          isConnected = ConnectionState.CONNECTED,
          friendRequests = emptyList(),
          acceptFriend = {}) {}
    }
    composeRule.onNodeWithTag("EmptyFriendsList").assertIsDisplayed()
  }

  // Test that a friend request list instanced with no connection is displayed
  @Test
  fun friendsRequestListWithNoConnectionIsDisplayed() {
    composeRule.activity.setContent {
      FriendsRequestList(
          isConnected = ConnectionState.OFFLINE, friendRequests = emptyList(), acceptFriend = {}) {}
    }
    composeRule.onNodeWithTag("ConnectionMissing").assertIsDisplayed()
  }
}
