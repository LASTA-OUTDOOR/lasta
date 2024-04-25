package com.lastaoutdoor.lasta.ui.screen.social

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.HikingLevel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.repository.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.SocialRepository
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import java.util.Date
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// Class mocking the social repository
class FakeSocialRepository : SocialRepository {

  var friends: ArrayList<UserModel> = ArrayList()
  var activities: ArrayList<ActivitiesDatabaseType> = ArrayList()
  var messages: ArrayList<String> = ArrayList()

  var sentRequest: ArrayList<String> = ArrayList()
  var receivedRequest: ArrayList<String> = ArrayList()

  // Change this to simulate the case were the function should return false
  var shouldWork = true

  override fun getFriends(userId: String): List<UserModel> {
    return friends
  }

  override fun getLatestFriendActivities(userId: String, days: Int): List<ActivitiesDatabaseType> {
    return activities
  }

  override fun getMessages(userId: String): List<String> {
    return messages
  }

  override fun sendFriendRequest(uid: String, email: String): Boolean {
    if (shouldWork) {
      sentRequest.add(email)
      return true
    }
    return false
  }

  override fun getFriendRequests(userId: String): List<UserModel> {
    return receivedRequest.map {
      UserModel(it, "name", "email", "photo", "bio", HikingLevel.BEGINNER)
    }
  }

  override fun acceptFriendRequest(source: String, requester: String) {
    friends.add(UserModel(requester, "name", "email", "photo", "bio", HikingLevel.BEGINNER))
    receivedRequest.remove(requester)
  }

  override fun declineFriendRequest(source: String, requester: String) {
    receivedRequest.remove(requester)
  }

  fun setMessages(messages: List<String>) {
    this.messages.clear()
    this.messages.addAll(messages)
  }

  fun setFriends(friend: List<UserModel>) {
    this.friends.clear()
    this.friends.addAll(friend)
  }

  fun setLatestFriendActivities(activities: List<ActivitiesDatabaseType>) {
    this.activities.clear()
    this.activities.addAll(activities)
  }

  fun setReceivedRequest(receivedRequest: List<String>) {
    this.receivedRequest.clear()
    this.receivedRequest.addAll(receivedRequest)
  }

  fun setSentRequest(sentRequest: List<String>) {
    this.sentRequest.clear()
    this.sentRequest.addAll(sentRequest)
  }

  fun clear() {
    friends.clear()
    activities.clear()
    messages.clear()
    sentRequest.clear()
    receivedRequest.clear()
  }
}

class FakeConnectivityRepository : ConnectivityRepository {

  private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.CONNECTED)
  override val connectionState: StateFlow<ConnectionState> = _connectionState

  fun setConnectionStateToTrue() {
    _connectionState.value = ConnectionState.CONNECTED
  }

  fun setConnectionStateToFalse() {
    _connectionState.value = ConnectionState.OFFLINE
  }
}

@HiltAndroidTest
@UninstallModules(AppModule::class)
class SocialScreenKtTest {

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

  @Test
  fun initialState() {

    // Set the content to the social screen
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      (socialViewModel.connectionRepo as FakeConnectivityRepository).setConnectionStateToFalse()
      SocialScreen(navController)
    }

    // Header (title)
    composeRule.onNodeWithTag("Header").assertIsDisplayed()

    // no button in header by default
    composeRule.onNodeWithTag("HeaderButton").assertIsNotDisplayed()

    // The tree tabs
    composeRule.onNodeWithText("Feed").assertIsDisplayed()
    composeRule.onNodeWithText("Friends").assertIsDisplayed()
    composeRule.onNodeWithText("Message").assertIsDisplayed()

    // First tab shows connection error
    composeRule.onNodeWithTag("ConnectionMissing").assertIsDisplayed()
  }

  @Test
  fun testTabSelection() {
    // Set the content to the social screen
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      (socialViewModel.connectionRepo as FakeConnectivityRepository).setConnectionStateToFalse()
      SocialScreen(navController)
    }

    // initial tab selection
    composeRule.onNodeWithText("Feed").assertIsSelected()

    // Click on the friends tab
    composeRule.onNodeWithText("Friends").performClick()

    // Check if the friends tab is selected
    composeRule.onNodeWithText("Friends").assertIsSelected()
    composeRule.onNodeWithText("Message").assertIsNotSelected()
    composeRule.onNodeWithText("Feed").assertIsNotSelected()
    composeRule.onNodeWithTag("TopButton").assertIsDisplayed()

    // Click on the message tab
    composeRule.onNodeWithText("Message").performClick()

    // Check if the message tab is selected
    composeRule.onNodeWithText("Friends").assertIsNotSelected()
    composeRule.onNodeWithText("Message").assertIsSelected()
    composeRule.onNodeWithText("Feed").assertIsNotSelected()
    composeRule.onNodeWithTag("TopButton").assertIsDisplayed()

    // Click on the feed tab
    composeRule.onNodeWithText("Feed").performClick()

    // Check if the feed tab is selected
    composeRule.onNodeWithText("Friends").assertIsNotSelected()
    composeRule.onNodeWithText("Message").assertIsNotSelected()
    composeRule.onNodeWithText("Feed").assertIsSelected()

    // Test the button in the header -> go to friends tab
    composeRule.onNodeWithText("Friends").performClick()
    // change the call back in viewmodel
    composeRule.onNodeWithTag("TopButton").assertIsDisplayed()
    socialViewModel.topButtonOnClick = {}
    composeRule.onNodeWithTag("TopButton").performClick()
  }

  @Test
  fun testEmptyStates() {
    // Test that the list
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      (socialViewModel.connectionRepo as FakeConnectivityRepository).setConnectionStateToTrue()
      SocialScreen(navController)
    }

    // Feed
    composeRule.onNodeWithTag("ConnectionMissing").assertIsNotDisplayed()
    composeRule.onNodeWithTag("FriendMissing").assertIsDisplayed()

    // Friends
    composeRule.onNodeWithText("Friends").performClick()
    composeRule.onNodeWithTag("FriendMissing").assertIsDisplayed()
    composeRule.onNodeWithTag("ConnectionMissing").assertIsNotDisplayed()

    // Message
    composeRule.onNodeWithText("Message").performClick()
    composeRule.onNodeWithTag("ConnectionMissing").assertIsNotDisplayed()
    composeRule.onNodeWithTag("MessageMissing").assertIsDisplayed()
  }

  @Test
  fun testFilledFeed() {

    // Fake data
    val act = ActivitiesDatabaseType.Trail(0, null, null, null, Date(), Date(), 0.0, 0, 0, 0)
    val activities = listOf(act, act, act)

    // Set the content to the social screen
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      (socialViewModel.connectionRepo as FakeConnectivityRepository).setConnectionStateToTrue()
      (socialViewModel.repository as FakeSocialRepository).setLatestFriendActivities(activities)
      SocialScreen(navController)
    }

    // Check that the add friend is not displayed
    composeRule.onNodeWithTag("FriendMissing").assertIsNotDisplayed()
    composeRule.onAllNodesWithTag("Activity").assertCountEquals(3)
  }

  @Test
  fun testFilledFriends() {

    // Fake data
    val friend = UserModel("1", "name", "email", "photo", "bio", HikingLevel.BEGINNER)
    val friends = listOf(friend, friend, friend)

    // Set the content to the social screen
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      (socialViewModel.connectionRepo as FakeConnectivityRepository).setConnectionStateToTrue()
      (socialViewModel.repository as FakeSocialRepository).setFriends(friends)
      SocialScreen(navController)
    }

    // go to the friends tab
    composeRule.onNodeWithText("Friends").performClick()

    // Check that the add friend is not displayed
    composeRule.onNodeWithTag("FriendMissing").assertIsNotDisplayed()
    composeRule.onAllNodesWithTag("Friend").assertCountEquals(3)
  }

  @Test
  fun testFilledMessages() {

    // Fake data
    val message = "message"
    val messages = listOf(message, message, message, message, message)

    // Set the content to the social screen
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      (socialViewModel.connectionRepo as FakeConnectivityRepository).setConnectionStateToTrue()
      (socialViewModel.repository as FakeSocialRepository).setMessages(messages)
      SocialScreen(navController)
    }

    // go to the friends tab
    composeRule.onNodeWithText("Message").performClick()

    // Check that the add friend is not displayed
    composeRule.onNodeWithTag("MessageMissing").assertIsNotDisplayed()
    composeRule.onAllNodesWithTag("Message").assertCountEquals(5)
  }
}
