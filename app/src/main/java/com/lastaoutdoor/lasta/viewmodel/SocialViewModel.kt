package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.repository.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.PreferencesRepository
import com.lastaoutdoor.lasta.repository.SocialRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SocialViewModel
@Inject
constructor(val repository: SocialRepository, val connectionRepo: ConnectivityRepository, val preferences: PreferencesRepository) :
    ViewModel() {

  var friendRequestFeedback: String by mutableStateOf("")

  private val numberOfDays = 7

  // is the top button visible
  var topButton by mutableStateOf(false)

  // returns all the messages of the user
  val messages = repository.getMessages()

  // returns all the friends of the users
  val friends = repository.getFriends()

  // returns all the activities done by friends in the last 7 days
  val latestFriendActivities = repository.getLatestFriendActivities(numberOfDays)

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
    viewModelScope.launch{
      // verify formatting of email
      friendRequestFeedback = if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        //get current user id from the flow
        val userId = preferences.userPreferencesFlow.first().uid
        // 2. send friend request
        if (repository.sendFriendRequest(userId ,email)) "Friend request sent"
        else "Failed to send friend request"

      } else {
        "The email address is not valid"
      }
  }
  }
}
