package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.utils.ConnectionState

@Composable
fun FriendsRequestList(
    isConnected: ConnectionState,
    friendRequests: List<UserModel>,
    acceptFriend: (UserModel) -> Unit,
    declineFriend: (UserModel) -> Unit
) {
  Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically) {
        Text(
            LocalContext.current.getString(R.string.friend_req),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp).testTag("FriendRequestTitle"),
        )
      }
  when {
    isConnected == ConnectionState.OFFLINE -> {
      ConnectionMissing()
    }
    friendRequests.isEmpty() -> {
      Column(
          modifier = Modifier.fillMaxSize().testTag("EmptyFriendsList"),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painterResource(id = R.drawable.no_friends),
                contentDescription = "No Activities",
                modifier = Modifier.size(85.dp).testTag("NoFriendsLogo"))
            Text(
                text = LocalContext.current.getString(R.string.no_friends_yet),
                style =
                    TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center),
                modifier = Modifier.padding(18.dp))
          }
    }
    else -> {
      LazyColumn {
        items(friendRequests.size) {
          FriendsRequestCard(
              friendRequests[it],
              { friend: UserModel -> acceptFriend(friend) },
              { friend: UserModel -> declineFriend(friend) })
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
