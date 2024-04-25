package com.lastaoutdoor.lasta.ui.screen.social.components

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
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.social.ConversationModel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.ui.navigation.LeafScreen
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@Composable
fun MessageList(navController: NavController, viewModel: SocialViewModel = hiltViewModel()) {
  val isConnected = viewModel.isConnected.collectAsState()
  when {
    isConnected.value == ConnectionState.OFFLINE -> {
      ConnectionMissing()
    }
    viewModel.messages.isEmpty() -> {
      if (viewModel.displayFriendPicker) {
        FriendPicker(navController = navController)
      }
      MessageMissing()
    }
    else -> {
      // call the friend picker (displayed when clicking on the email icon)
      if (viewModel.displayFriendPicker) {
        FriendPicker(navController = navController)
      }

      // refresh the message list
      viewModel.refreshMessages()

      // list of messages
      LazyColumn {
        items(viewModel.messages.size) { MessageCard(viewModel.messages[it], navController) }
      }
    }
  }
}

@Composable
fun MessageCard(
    message: ConversationModel,
    navController: NavController,
    viewModel: SocialViewModel = hiltViewModel()
) {

  val friend: UserModel? = message.users.firstOrNull { it.userId != viewModel.userId }
  if (friend == null) {
    return
  }

  Card(
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      modifier =
          Modifier.height(height = 100.dp)
              .fillMaxWidth()
              .padding(8.dp)
              .testTag("Message")
              .clickable {
                navController.navigate(LeafScreen.Conversation.route + "/${friend.userId}")
              }) {
        Column(modifier = Modifier.padding(8.dp)) {
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProfilePicture(friend.profilePictureUrl ?: "")
                Text(
                    text = friend.userName ?: "Name error",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontWeight = FontWeight.Bold)
              }

          Text(text = message.lastMessage?.content ?: "", overflow = TextOverflow.Ellipsis)
        }
      }
}

@Composable
private fun ProfilePicture(url: String) {
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
