package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.repository.SocialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(private val repository: SocialRepository) : ViewModel() {

  private val numberOfDays = 7

  var messages = repository.getMessages()

  // returns all the friends of the users
  var friends = repository.getFriends()

  // returns all the activities done by friends in the last 7 days
  var latestFriendActivities = repository.getLatestFriendActivities(numberOfDays)

  val isConnected = repository.isConnected
}
