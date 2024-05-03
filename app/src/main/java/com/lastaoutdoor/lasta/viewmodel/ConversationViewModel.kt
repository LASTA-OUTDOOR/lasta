package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.app.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import com.lastaoutdoor.lasta.repository.db.SocialDBRepository
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel
class ConversationViewModel
@Inject
constructor(
    private val userRepository: UserDBRepository,
    val repository: SocialDBRepository,
    val connectionRepo: ConnectivityRepository,
    val preferences: PreferencesRepository
) : ViewModel() {

  private val _user = MutableStateFlow<UserModel>(UserModel(""))
  val user = _user

  private val _friend = MutableStateFlow<UserModel>(UserModel(""))
  val friend = _friend

  // current user id
  val userId: String = runBlocking { preferences.userPreferencesFlow.first().user.userId }

  // friend user id
  var friendUserId: String by mutableStateOf("")

  // The conversation between the user and the friend
  var conversation: ConversationModel? by mutableStateOf(null)

  // Display the send message dialog
  var showSendMessageDialog: Boolean by mutableStateOf(false)

  init {
    viewModelScope.launch { preferences.userPreferencesFlow.collect { _user.value = it.user } }
  }

  // refresh the conversation
  fun updateConversation() {
    viewModelScope.launch {
      _friend.value = userRepository.getUserById(friendUserId) ?: UserModel("")
      if (friendUserId.isNotEmpty())
          conversation = repository.getConversation(_user.value, _friend.value)
    }
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
    viewModelScope.launch {
      if (message.isNotEmpty()) {
        repository.sendMessage(userId, friendUserId, message)
        updateConversation()
      }
    }
  }
}
