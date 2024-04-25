package com.lastaoutdoor.lasta.ui.screen.social

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class AddFriendScreenKtTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Viewmodel and repository
  private lateinit var socialViewModel: SocialViewModel

  // Navigation controller
  private lateinit var navController: NavController

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
    // set up the navigation controller
    navController = NavHostController(composeRule.activity)
  }

  // Default layout of the page
  @Test
  fun initialState() {

    // Set the content to the social screen
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      (socialViewModel.connectionRepo as FakeConnectivityRepository).setConnectionStateToFalse()
      AddFriendScreen(navController)
    }

    // Header (title)
    composeRule.onNodeWithTag("Header").assertIsDisplayed()

    // Text info
    composeRule.onNodeWithTag("SubHeader").assertIsDisplayed()

    // TextField
    composeRule.onNodeWithTag("EmailTextField").assertIsDisplayed()

    // Button
    composeRule.onNodeWithTag("SubmitButton").assertIsDisplayed()
  }

  // Test the friend request list
  @Test
  fun testFriendRequestsNoInternet() {

    // Set the content to the social screen
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      (socialViewModel.connectionRepo as FakeConnectivityRepository).setConnectionStateToFalse()
      NotificationsScreen(navController)
    }

    // Check that the title is displayed
    composeRule.onNodeWithTag("FriendRequestTitle").assertIsDisplayed()

    // No internet connection should be displayed
    composeRule.onNodeWithTag("ConnectionMissing").assertIsDisplayed()

    // Activate internet connection
    (socialViewModel.connectionRepo as FakeConnectivityRepository).setConnectionStateToTrue()
  }

  // Test the friend request list with no requests
  @Test
  fun testFriendRequestsNoRequests() {

    // Set the content to the social screen
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      (socialViewModel.connectionRepo as FakeConnectivityRepository).setConnectionStateToTrue()
      NotificationsScreen(navController)
    }

    // Check that the title is displayed
    composeRule.onNodeWithTag("FriendRequestTitle").assertIsDisplayed()

    // No friend requests should be displayed
    composeRule.onNodeWithTag("NoFriendRequest").assertIsDisplayed()
  }

  // Test the friend request list with requests
  @Test
  fun testFriendRequests() {

    // Set the content to the social screen
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      (socialViewModel.connectionRepo as FakeConnectivityRepository).setConnectionStateToTrue()
      (socialViewModel.repository as FakeSocialRepository).clear()
      (socialViewModel.repository as FakeSocialRepository).setReceivedRequest(listOf("1", "2", "3"))
      socialViewModel.refreshFriendRequests()
      NotificationsScreen(navController)
      socialViewModel.refreshFriendRequests()
    }

    // check that the title is displayed
    composeRule.onNodeWithTag("FriendRequestTitle").assertIsDisplayed()

    // the no friend request should not be displayed
    composeRule.onNodeWithTag("NoFriendRequest").assertIsNotDisplayed()

    // There should be 3 friend requests
    composeRule.onAllNodesWithTag("FriendRequest").assertCountEquals(3)
  }
}
