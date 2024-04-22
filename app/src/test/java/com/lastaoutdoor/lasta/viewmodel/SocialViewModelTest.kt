package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.social.MessageModel
import com.lastaoutdoor.lasta.data.model.user.HikingLevel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.data.model.user.UserPreferences
import com.lastaoutdoor.lasta.repository.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.PreferencesRepository
import com.lastaoutdoor.lasta.repository.SocialRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainCoroutineRule(private val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {

  override fun starting(description: Description?) {
    super.starting(description)
    Dispatchers.setMain(dispatcher)
  }

  override fun finished(description: Description?) {
    super.finished(description)
    Dispatchers.resetMain()
  }
}

@OptIn(ExperimentalCoroutinesApi::class)
class FakeSocialRepository : SocialRepository {

  var friends: ArrayList<UserModel> = ArrayList()
  var activities: ArrayList<ActivitiesDatabaseType> = ArrayList()
  var messages: ArrayList<MessageModel> = ArrayList()

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

  override fun getMessages(userId: String): List<MessageModel> {
    return messages
  }

  override fun sendFriendRequest(uid: String, email: String): Boolean {
    if (shouldWork) {
      sentRequest += (email)
      return true
    }
    return false
  }

  override fun getFriendRequests(userId: String): List<UserModel> {
    return receivedRequest.map { UserModel(it, "name", "email", "photo", HikingLevel.BEGINNER) }
  }

  override fun acceptFriendRequest(source: String, requester: String) {
    friends.add(UserModel(requester, "name", "email", "photo", HikingLevel.BEGINNER))
    receivedRequest.remove(requester)
  }

  override fun declineFriendRequest(source: String, requester: String) {
    receivedRequest.remove(requester)
  }

  fun setMessages(messages: List<MessageModel>) {
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

  fun setReceivedRequest(requests: List<String>) {
    this.receivedRequest.clear()
    this.receivedRequest.addAll(requests)
  }

  fun setSentRequest(requests: List<String>) {
    this.sentRequest.clear()
    this.sentRequest.addAll(requests)
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

class FakePreferencesRepository(override val userPreferencesFlow: Flow<UserPreferences>) :
    PreferencesRepository {

  override suspend fun updateIsLoggedIn(isLoggedIn: Boolean) {}

  override suspend fun updateUserInfo(user: UserModel?) {}

  override suspend fun updateHikingLevel(hikingLevel: HikingLevel) {}

  override suspend fun clearPreferences() {}
}

class SocialViewModelTest {

  private val repo = FakeSocialRepository()
  private lateinit var viewModel: SocialViewModel

  // Mock the user preferences flow
  private val userPreferencesFlow =
      MutableStateFlow(UserPreferences(true, "email", "name", "photo", "", HikingLevel.BEGINNER))

  @Before
  fun setUp() {
    viewModel =
        SocialViewModel(
            repo, FakeConnectivityRepository(), FakePreferencesRepository(userPreferencesFlow))
  }

  @Test
  fun `Default values`() {

    runTest {
      viewModel.isConnected.take(1).collect() { state ->
        assertEquals(ConnectionState.OFFLINE, state)
      }
    }
    assert(viewModel.getNumberOfDays() == 7)
    assertFalse(viewModel.topButton)
    assertTrue(viewModel.messages.isEmpty())
    assertTrue(viewModel.friends.isEmpty())
    assertTrue(viewModel.latestFriendActivities.isEmpty())
    assert(viewModel.topButtonIcon == Icons.Filled.Email)
  }

  @Test
  fun `Show top button`() {
    viewModel.showTopButton(Icons.Filled.Add, {})
    assertTrue(viewModel.topButton)
    assert(viewModel.topButtonIcon == Icons.Filled.Add)

    viewModel.hideTopButton()
    assertFalse(viewModel.topButton)
  }

  @ExperimentalCoroutinesApi @get:Rule var mainCoroutineRule = MainCoroutineRule()
}
