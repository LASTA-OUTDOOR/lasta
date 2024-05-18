package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.utils.ConnectionState

@Composable
fun FriendsList(
    isConnected: ConnectionState,
    friends: List<UserModel>,
    displayAddFriendDialog: Boolean,
    friendRequestFeedback: String,
    clearFriendRequestFeedback: () -> Unit,
    hideAddFriendDialog: () -> Unit,
    requestFriend: (String) -> Unit,
    refreshFriends: () -> Unit,
    navigateToFriendProfile: (String) -> Unit
) {

  LaunchedEffect(Unit) { refreshFriends() }
  when {
    isConnected == ConnectionState.OFFLINE -> {
      ConnectionMissing()
    }
    friends.isEmpty() -> {
      // add friend dialog when you click on the add friend button
      if (displayAddFriendDialog)
          AddFriendDialog(
              friendRequestFeedback, clearFriendRequestFeedback, hideAddFriendDialog, requestFriend)
      FriendsMissing()
    }
    else -> {
      // add friend dialog when you click on the add friend button
      if (displayAddFriendDialog)
          AddFriendDialog(
              friendRequestFeedback, clearFriendRequestFeedback, hideAddFriendDialog, requestFriend)
      LazyColumn {
        items(friends.size) {
          FriendsCard(friends[it]) { navigateToFriendProfile(friends[it].userId) }
        }
      }
    }
  }
}

@Composable
fun FriendsCard(friend: UserModel, navToFriend: () -> Unit) {
  Card(
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      modifier =
          Modifier.height(height = 100.dp)
              .fillMaxWidth()
              .padding(8.dp)
              .testTag("FriendCard")
              .clickable { navToFriend() }) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {

          // Profile picture
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

          // Text information
          Column(modifier = Modifier.padding(8.dp)) {
            Text(text = friend.userName ?: "Name error", fontWeight = FontWeight.Bold)
            // display the user's sport preference
            Row(modifier = Modifier.testTag("FriendPrefActivity")) {
              Text(text = friend.prefActivity.toString() + " - ")
              // print the level of the levels index of prefActivity
              if (friend.prefActivity == ActivityType.HIKING) {
                Text(text = friend.levels.hikingLevel.toString())
              } else if (friend.prefActivity == ActivityType.CLIMBING) {
                Text(text = friend.levels.climbingLevel.toString())
              } else if (friend.prefActivity == ActivityType.BIKING) {
                Text(text = friend.levels.bikingLevel.toString())
              }

              Text(text = " - " + friend.language.toString())
            }
          }
        }
      }
}
