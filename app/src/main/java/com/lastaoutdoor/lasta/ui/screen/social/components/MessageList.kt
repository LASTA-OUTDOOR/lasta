package com.lastaoutdoor.lasta.ui.screen.social.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.utils.ConnectionState

@Composable
fun MessageList(
    isConnected: ConnectionState,
    messages: List<ConversationModel>,
    refreshMessages: () -> Unit,
    friends: List<UserModel>,
    hideFriendPicker: () -> Unit,
    navigateToConversation: (String) -> Unit,
    displayFriendPicker: Boolean,
    changeDisplayFriendPicker: () -> Unit
) {

  LaunchedEffect(Unit) { refreshMessages() }

  when {
    isConnected == ConnectionState.OFFLINE -> {
      ConnectionMissing()
    }
    messages.isEmpty() -> {
      if (displayFriendPicker) {
        FriendPicker(friends, hideFriendPicker, navigateToConversation)
      }
      MessageMissing(changeDisplayFriendPicker)
    }
    else -> {
      // call the friend picker (displayed when clicking on the email icon)
      if (displayFriendPicker) {
        FriendPicker(friends, hideFriendPicker, navigateToConversation)
      }
      // list of messages
      LazyColumn {
        items(messages.size) { messageIndex ->
          val friendId =
              messages[messageIndex]
                  .members
                  .firstOrNull { friends.map { it.userId }.contains(it.userId) }
                  ?.userId
          if (friendId == null) {
            Log.e("MessageList", "Friend not found in friends list")
            return@items
          }
          MessageCard(messages[messageIndex], friendId, navigateToConversation)
        }
      }
    }
  }
}

@Composable
fun MessageCard(
    message: ConversationModel,
    friendId: String,
    navigateToConversation: (String) -> Unit
) {

  val friend = message.members.firstOrNull { it.userId == friendId } ?: return

  Card(
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      modifier =
          Modifier.height(height = 100.dp)
              .fillMaxWidth()
              .padding(8.dp)
              .testTag("MessageCard")
              .clickable { navigateToConversation(friend.userId) }) {
        Column(modifier = Modifier.padding(8.dp)) {
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProfilePicture(friend.profilePictureUrl)
                Text(
                    text = friend.userName,
                    modifier = Modifier.align(alignment = Alignment.CenterVertically),
                    fontWeight = FontWeight.Bold)
              }
          // if last message is an activity, display the activity name
          val isActivityShared = message.lastMessage?.content?.startsWith("|$@!|") ?: false
          val messageParts = message.lastMessage?.content?.split("|") ?: emptyList()
          val messageToDisplay =
              if (isActivityShared) messageParts[3] else message.lastMessage?.content
          Text(text = messageToDisplay ?: "", overflow = TextOverflow.Ellipsis)
        }
      }
}

@Composable
fun ProfilePicture(url: String) {
  AsyncImage(
      model =
          ImageRequest.Builder(LocalContext.current)
              .data(url)
              .crossfade(true)
              .memoryCachePolicy(CachePolicy.ENABLED)
              .build(),
      placeholder = painterResource(R.drawable.default_profile_icon),
      contentDescription = "Profile Picture",
      contentScale = ContentScale.Crop,
      error = painterResource(R.drawable.default_profile_icon),
      modifier = Modifier.clip(RoundedCornerShape(100.dp)).size(30.dp).fillMaxHeight())
}
