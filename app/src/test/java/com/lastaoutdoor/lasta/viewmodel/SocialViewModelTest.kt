package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.test.core.app.ActivityScenario.launch
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.repository.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.SocialRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.job
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FakeSocialRepository : SocialRepository {

  var friends: ArrayList<UserModel> = ArrayList()
  var activities: ArrayList<ActivitiesDatabaseType> = ArrayList()
  var messages: ArrayList<String> = ArrayList()

  override fun getFriends(): List<UserModel>? {
    return friends
  }

  override fun getLatestFriendActivities(days: Int): List<ActivitiesDatabaseType>? {
    return activities
  }

  override fun getMessages(): List<String>? {
    return messages
  }

  override fun setMessages(messages: List<String>) {
    this.messages.clear()
    this.messages.addAll(messages)
  }

  override fun setFriends(friend: List<UserModel>) {
    this.friends.clear()
    this.friends.addAll(friend)
  }

  override fun setLatestFriendActivities(activities: List<ActivitiesDatabaseType>) {
    this.activities.clear()
    this.activities.addAll(activities)
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

class SocialViewModelTest {

  private val repo = FakeSocialRepository()
  private lateinit var viewModel: SocialViewModel

  @Before
  fun setUp() {
    viewModel = SocialViewModel(repo, FakeConnectivityRepository())
  }

  @Test
  fun `Default values`() {

    runTest {
      viewModel.isConnected.take(1).collect() { state ->
        assertEquals(ConnectionState.OFFLINE, state)
      }
    }
    assert(viewModel.getNumberOfDays() == 7)
    assertFalse(viewModel.friendButton)
    assertTrue(viewModel.messages.isNullOrEmpty())
    assertTrue(viewModel.friends.isNullOrEmpty())
    assertTrue(viewModel.latestFriendActivities.isNullOrEmpty())
    assert(viewModel.topButtonText == "Default button")
  }

  @Test
  fun `Show top button`() {
    viewModel.showTopButton("Add friend", {})
    assertTrue(viewModel.friendButton)
    assert(viewModel.topButtonText == "Add friend")

    viewModel.hideTopButton()
    assertFalse(viewModel.friendButton)
  }
}
