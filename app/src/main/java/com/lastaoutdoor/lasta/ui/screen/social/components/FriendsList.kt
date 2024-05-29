package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.utils.ConnectionState

@Composable
fun FriendsList(
    isConnected: ConnectionState,
    friends: List<UserModel>,
    displayAddFriendDialog: Boolean,
    friendRequestFeedback: String,
    friendSuggestions: List<UserModel>,
    clearFriendRequestFeedback: () -> Unit,
    hideAddFriendDialog: () -> Unit,
    fetchFriendsSuggestions: (String) -> Unit,
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
              friendRequestFeedback,
              friendSuggestions,
              clearFriendRequestFeedback,
              hideAddFriendDialog,
              requestFriend,
              fetchFriendsSuggestions)
      FriendsMissing(text = LocalContext.current.getString(R.string.no_friend_activities))
    }
    else -> {
      // add friend dialog when you click on the add friend button
      if (displayAddFriendDialog)
          AddFriendDialog(
              friendRequestFeedback,
              friendSuggestions,
              clearFriendRequestFeedback,
              hideAddFriendDialog,
              requestFriend,
              fetchFriendsSuggestions)
      LazyColumn {
        items(friends.size) {
          FriendsCard(friends[it], friends) { navigateToFriendProfile(friends[it].userId) }
        }
      }
    }
  }
}

@Composable
fun FriendsCard(friend: UserModel, friendList: List<UserModel>, navToFriend: () -> Unit) {
  Card(
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      modifier =
          Modifier.height(height = 140.dp)
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
          Row(
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(text = friend.userName, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                      text = friend.language.resourcesToString(LocalContext.current),
                      modifier = Modifier.padding(5.dp))
                  Image(
                      painter = friend.language.getIcon(),
                      contentDescription = "language logo",
                      modifier = Modifier.size(25.dp, 25.dp))
                }
              }
        }
        Row(
            modifier = Modifier.testTag("FriendPrefActivity").fillMaxWidth().padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
              // displays the user's sport preference
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.testTag("friendInfo").padding(5.dp),
                    text = friend.descrText(),
                    style = MaterialTheme.typography.labelMedium)
                Image(
                    painter = friend.prefActivity.getIcon(),
                    contentDescription = "Preferred activity logo",
                    modifier = Modifier.size(25.dp, 25.dp))
              }
              // displays the number of friends in common
              val friendsInCommonText =
                  when (friend.getCommonFriends(friendList)) {
                    0 -> LocalContext.current.getString(R.string.no_friends_in_common)
                    1 -> LocalContext.current.getString(R.string.one_friends_in_common)
                    else ->
                        "${friend.getCommonFriends(friendList)} ${LocalContext.current.getString(R.string.friends_in_common)}"
                  }
              Row {
                Text(
                    text = friendsInCommonText,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.End)
              }
            }
      }
}
