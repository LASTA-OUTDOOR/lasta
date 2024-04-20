package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.repository.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.SocialRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SocialViewModel
@Inject
constructor(val repository: SocialRepository, val connectionRepo: ConnectivityRepository) :
    ViewModel() {

  private val numberOfDays = 7

  //
  var friendButton by mutableStateOf(false)

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

  var topButtonText by mutableStateOf("Default button")
  var topButtonOnClick by mutableStateOf({})

  fun getNumberOfDays(): Int {
    return numberOfDays
  }

  fun showTopButton(text: String, onClick: () -> Unit) {
    // show add friend button
    topButtonText = text
    topButtonOnClick = { onClick() }
    friendButton = true
  }

  fun hideTopButton() {
    // hide add friend button
    friendButton = false
  }
}
