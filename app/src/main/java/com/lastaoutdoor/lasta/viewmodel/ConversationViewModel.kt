package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.repository.app.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import com.lastaoutdoor.lasta.repository.db.SocialDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@HiltViewModel
class ConversationViewModel
@Inject
constructor(
    val repository: SocialDBRepository,
    val connectionRepo: ConnectivityRepository,
    val preferences: PreferencesRepository
) : ViewModel() {

  // current user id
  val userId: String = runBlocking { preferences.userPreferencesFlow.first().user.userId }

  // friend user id
  var friendUserId: String by mutableStateOf("")

  // The conversation between the user and the friend
  var conversation: ConversationModel? by mutableStateOf(null)

  // Display the send message dialog
  var showSendMessageDialog: Boolean by mutableStateOf(false)

  // refresh the conversation
  fun updateConversation() {
    if (friendUserId.isEmpty()) return
    conversation = repository.getConversation(userId, friendUserId)
  }

  // update the friend user id
  fun updateFriendUserId(friendId: String) {
    friendUserId = friendId
  }

  // show the send message dialog
  fun showSendMessageDialog() {
    showSendMessageDialog = true
  }

  // hide the send message dialog
  fun hideSendMessageDialog() {
    showSendMessageDialog = false
  }

  // send a message
  fun send(message: String) {
    println("VM : sending the message")
    if (message.isEmpty()) return
    println("VM : notNUll")
    repository.sendMessage(userId, friendUserId, message)
    updateConversation()
  }
}
