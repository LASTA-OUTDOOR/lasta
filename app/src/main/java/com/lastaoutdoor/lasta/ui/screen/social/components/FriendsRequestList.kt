package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@Composable
fun FriendsRequestList(viewModel: SocialViewModel = hiltViewModel()) {
  Text(
      "Friend requests",
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(8.dp).testTag("FriendRequestTitle"))
  val isConnected = viewModel.isConnected.collectAsState()
  when {
    isConnected.value == ConnectionState.OFFLINE -> {
      ConnectionMissing()
    }
    viewModel.friendsRequest.isEmpty() -> {
      Text(
          "You do not have any friends request yet",
          Modifier.padding(8.dp).testTag("NoFriendRequest"))
    }
    else -> {
      LazyColumn {
        items(viewModel.friendsRequest.size) {
          FriendsRequestCard(
              viewModel.friendsRequest[it],
              { friend: UserModel -> viewModel.acceptFriend(friend) },
              { friend: UserModel -> viewModel.declineFriend(friend) })
        }
      }
    }
  }
}

@Composable
fun FriendsRequestCard(
    friend: UserModel,
    accept: (UserModel) -> Unit,
    decline: (UserModel) -> Unit
) {
  Card(
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      modifier =
          Modifier.height(height = 100.dp).fillMaxWidth().padding(8.dp).testTag("FriendRequest")) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxHeight().fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
              AsyncImage(
                  model =
                      ImageRequest.Builder(LocalContext.current)
                          .data(friend.profilePictureUrl)
                          .crossfade(true)
                          .memoryCachePolicy(CachePolicy.ENABLED)
                          .build(),
                  placeholder = painterResource(R.drawable.default_profile_icon),
                  contentDescription = "Profile Picture",
                  contentScale = ContentScale.Crop,
                  error = painterResource(R.drawable.default_profile_icon),
                  modifier = Modifier.clip(RoundedCornerShape(100.dp)).size(60.dp).fillMaxHeight())

              Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                Text(friend.userName ?: "Unknown user")
                Row {
                  IconButton(
                      modifier = Modifier.testTag("AcceptButton"), onClick = { accept(friend) }) {
                        Icon(Icons.Filled.Check, contentDescription = "Accept the friend request")
                      }
                  IconButton(
                      modifier = Modifier.testTag("DeclineButton"), onClick = { decline(friend) }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Decline the friend request")
                      }
                }
              }
            }
      }
}
