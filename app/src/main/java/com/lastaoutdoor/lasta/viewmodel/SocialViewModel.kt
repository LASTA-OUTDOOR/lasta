package com.lastaoutdoor.lasta.viewmodel

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.db.TokenDBRepositoryImpl
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.app.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import com.lastaoutdoor.lasta.repository.db.SocialDBRepository
import com.lastaoutdoor.lasta.repository.db.TokenDBRepository
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SocialViewModel
@Inject
constructor(
  @ApplicationContext private val context: Context,
  val repository: SocialDBRepository,
  private val userDBRepo: UserDBRepository,
  val tokenDBRepo: TokenDBRepository,
  connectionRepo: ConnectivityRepository,
  val preferences: PreferencesRepository
) : ViewModel() {

  // get the user id
  var user: UserModel by mutableStateOf(UserModel(""))

  // Get all the friends request
  var friendRequests: List<UserModel> by mutableStateOf(emptyList())

  // feedback message for the friend request
  var friendRequestFeedback: String by mutableStateOf("")

  // Is there a notification for a friend request
  var hasFriendRequest: Boolean = friendRequests.isNotEmpty()

  // number of days to consider for the latest activities
  private val numberOfDays = 7

  // is the top button visible
  var topButton by mutableStateOf(false)

  // Display the list of friends that can be messaged
  var displayFriendPicker by mutableStateOf(false)

  // Display the dialog to add a friend
  var displayAddFriendDialog by mutableStateOf(false)

  // returns all the friends of the users
  var friends: List<UserModel> by mutableStateOf(emptyList())

  // returns all the existing conversations
  var messages: List<ConversationModel> by mutableStateOf(emptyList())

  // returns all the activities done by friends in the last 7 days
  val latestFriendActivities: List<UserActivity> by mutableStateOf(emptyList())

  // Fetch connection info from the repository, set isConnected to true if connected, false
  // otherwise
  var isConnected: StateFlow<ConnectionState> =
      connectionRepo.connectionState.stateIn(
          initialValue = ConnectionState.OFFLINE,
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000))

  var topButtonIcon by mutableStateOf(Icons.Filled.Email)
  var topButtonOnClick by mutableStateOf({})

  init {
    viewModelScope.launch {
      preferences.userPreferencesFlow.collect { user = it.user }
      refreshFriends()
      refreshFriendRequests()
      refreshMessages()
    }
  }

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
          if (android.util.Patterns.EMAIL_ADDRESS != null &&
              android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // get current user id from the flow
            val userId = preferences.userPreferencesFlow.first().user.userId
            val friendId = userDBRepo.getUserByEmail(email)?.userId
            if (friendId == null) {
              context.getString(R.string.no_account)
            } else if (userId == friendId) {
              context.getString(R.string.no_fr_yourself)
            } else if (friendRequests.any { it.userId == friendId }) {
              context.getString(R.string.no_fr_twice)
            } else if (friends.any { it.userId == friendId }) {
              context.getString(R.string.no_fr)
            } else {
              repository.sendFriendRequest(userId, friendId)
              context.getString(R.string.fr_req_sent)
            }
          } else {
            context.getString(R.string.inv_email)
          }
    }
  }

  fun acceptFriend(friend: UserModel) {
    viewModelScope.launch {
      repository.acceptFriendRequest(user.userId, friend.userId)
      refreshFriendRequests()
      // update the list of friends
      refreshFriends()
    }
  }

  // Decline a friend request
  fun declineFriend(friend: UserModel) {
    viewModelScope.launch {
      repository.declineFriendRequest(user.userId, friend.userId)
      // update the list of friends
      refreshFriendRequests()
    }
  }

  // Refresh the list of friends request
  fun refreshFriendRequests() {
    viewModelScope.launch {
      friendRequests = repository.getFriendRequests(user.userId)
      hasFriendRequest = friendRequests.isNotEmpty()
    }
  }

  // Refresh the list of friends
  fun refreshFriends() {
    viewModelScope.launch {
      val userModel = userDBRepo.getUserById(user.userId)
      preferences.updateFriends(userModel?.friends ?: emptyList())
      friends = repository.getFriends(userModel?.friends ?: emptyList())
    }
  }

  // Refresh the list of friends
  fun refreshMessages() {
    viewModelScope.launch { messages = repository.getAllConversations(user.userId) }
  }

  // This displays the friend picker (in order to send a message)
  fun displayFriendPicker() {
    displayFriendPicker = true
  }

  // Hide the friend picker (dismiss request)
  fun hideFriendPicker() {
    displayFriendPicker = false
  }

  // Display the dialog to add a friend
  fun displayAddFriendDialog() {
    displayAddFriendDialog = true
  }

  // Hide the dialog to add a friend
  fun hideAddFriendDialog() {
    displayAddFriendDialog = false
  }

  // Clear the feedback message for the friend request
  fun clearFriendRequestFeedback() {
    friendRequestFeedback = ""
  }
}
