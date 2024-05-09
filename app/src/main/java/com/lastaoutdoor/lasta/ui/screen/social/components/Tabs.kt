package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.utils.ConnectionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabMenu(
    isConnected: ConnectionState,
    friends: List<UserModel>,
    messages: List<ConversationModel>,
    latestFriendActivities: List<UserActivity>,
    addFriendDialog: Boolean,
    friendRequestFeedback: String,
    isDisplayedFriendPicker: Boolean,
    refreshMessages: () -> Unit,
    clearFriendRequestFeedback: () -> Unit,
    hideAddFriendDialog: () -> Unit,
    requestFriend: (String) -> Unit,
    refreshFriends: () -> Unit,
    showTopButton: (ImageVector, () -> Unit) -> Unit,
    hideTopButton: () -> Unit,
    displayAddFriendDialog: () -> Unit,
    displayFriendPicker: () -> Unit,
    hideFriendPicker: () -> Unit,
    changeDisplayFriendPicker: () -> Unit,
    navigateToConversation: (String) -> Unit,
    navigateToFriendProfile: (String) -> Unit
) {

  var state by remember { mutableIntStateOf(0) }
  val titles =
      listOf(
          LocalContext.current.getString(R.string.feed),
          LocalContext.current.getString(R.string.friends),
          LocalContext.current.getString(R.string.message))

  Column {
    PrimaryTabRow(selectedTabIndex = state) {
      titles.forEachIndexed { index, title ->
        Tab(
            selected = state == index,
            onClick = { state = index },
            modifier = Modifier.testTag("TabNumber$index"),
            text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) })
      }
    }
  }

  // use the state variable to choose which composable to display
  when (state) {
    0 -> {
      hideTopButton()
      FriendsActivityList(isConnected, latestFriendActivities)
    }
    1 -> {
      showTopButton(Icons.Filled.Add) { displayAddFriendDialog() }
      FriendsList(
          isConnected,
          friends,
          addFriendDialog,
          friendRequestFeedback,
          clearFriendRequestFeedback,
          hideAddFriendDialog,
          requestFriend,
          refreshFriends,
          navigateToFriendProfile)
    }
    2 -> {
      showTopButton(Icons.Filled.Email) { displayFriendPicker() }
      MessageList(
          isConnected,
          messages,
          refreshMessages,
          friends,
          hideFriendPicker,
          navigateToConversation,
          isDisplayedFriendPicker,
          changeDisplayFriendPicker)
    }
  }
}
