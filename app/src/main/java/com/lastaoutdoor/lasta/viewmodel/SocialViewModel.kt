package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.repository.SocialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// TODO: put this in data package
data class TopButton(val text: String, val onClick: () -> Unit)

@HiltViewModel
class SocialViewModel @Inject constructor(private val repository: SocialRepository) : ViewModel() {

  private val numberOfDays = 7

  var friendButton by mutableStateOf(false)

  var messages = repository.getMessages()

  // returns all the friends of the users
  var friends = repository.getFriends()

  // returns all the activities done by friends in the last 7 days
  var latestFriendActivities = repository.getLatestFriendActivities(numberOfDays)

  val isConnected = repository.isConnected

  var topButtonText by mutableStateOf("Default button")
  var topButtonOnClick by mutableStateOf({})

  fun showTopButton(text: String, onClick: () -> Unit) {
    // show add friend button
    topButtonText = text
    topButtonOnClick = onClick
    friendButton = true
  }

  fun hideTopButton() {
    // hide add friend button
    friendButton = false
  }
}
