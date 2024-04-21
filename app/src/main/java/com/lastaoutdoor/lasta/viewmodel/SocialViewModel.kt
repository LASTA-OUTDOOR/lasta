package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.repository.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.PreferencesRepository
import com.lastaoutdoor.lasta.repository.SocialRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel
class SocialViewModel
@Inject
constructor(
    val repository: SocialRepository,
    val connectionRepo: ConnectivityRepository,
    val preferences: PreferencesRepository
) : ViewModel() {

  // get the user id
  val userId = runBlocking { preferences.userPreferencesFlow.first().uid }

  // Get all the friends request
  var friendsRequest: List<UserModel> by mutableStateOf(repository.getFriendRequests(userId))

  // feedback message for the friend request
  var friendRequestFeedback: String by mutableStateOf("")

  // Is there a notification for a friend request
  var hasFriendRequest: Boolean by mutableStateOf(friendsRequest.isNotEmpty())

  // number of days to consider for the latest activities
  private val numberOfDays = 7

  // is the top button visible
  var topButton by mutableStateOf(false)

  // returns all the messages of the user
  var messages = repository.getMessages(userId)

  // returns all the friends of the users
  var friends = repository.getFriends(userId)

  // returns all the activities done by friends in the last 7 days
  val latestFriendActivities = repository.getLatestFriendActivities(userId, numberOfDays)

  // Fetch connection info from the repository, set isConnected to true if connected, false
  // otherwise
  var isConnected: StateFlow<ConnectionState> =
      connectionRepo.connectionState.stateIn(
          initialValue = ConnectionState.OFFLINE,
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000))

  var topButtonIcon by mutableStateOf(Icons.Filled.Email)
  var topButtonOnClick by mutableStateOf({})

  fun getNumberOfDays(): Int {
    return numberOfDays
  }

  fun showTopButton(ico: ImageVector, onClick: () -> Unit) {
    // show add friend button
    topButtonIcon = ico
    topButtonOnClick = { onClick() }
    topButton = true
  }

  fun hideTopButton() {
    // hide add friend button
    topButton = false
  }

  // called after a click on the add friend button
  fun requestFriend(email: String) {
    viewModelScope.launch {
      // verify formatting of email
      friendRequestFeedback =
          if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // get current user id from the flow
            val userId = preferences.userPreferencesFlow.first().uid
            // 2. send friend request
            if (repository.sendFriendRequest(userId, email)) "Friend request sent"
            else "Failed to send friend request"
          } else {
            "The email address is not valid"
          }
    }
  }

  fun acceptFriend(friend: UserModel) {
    viewModelScope.launch {
      val userId = preferences.userPreferencesFlow.first().uid
      repository.acceptFriendRequest(userId, friend.userId)
    }
    // update the list of friends
    refreshFriendRequests()
  }

  // Decline a friend request
  fun declineFriend(friend: UserModel) {
    viewModelScope.launch {
      val userId = preferences.userPreferencesFlow.first().uid
      repository.declineFriendRequest(userId, friend.userId)
    }
    // update the list of friends
    refreshFriendRequests()
  }

  // Refresh the list of friends request
  fun refreshFriendRequests() {
    viewModelScope.launch {
      friendsRequest = repository.getFriendRequests(userId)
      hasFriendRequest = friendsRequest.isNotEmpty()
    }
  }

  // Refresh the list of friends
  fun refreshFriends() {
    viewModelScope.launch { friends = repository.getFriends(userId) }
  }
}
