package com.lastaoutdoor.lasta.ui.screen.social.components

import android.media.tv.TvContract.Channels.Logo
import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserLevel
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
      Modifier
          .height(height = 130.dp)
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
              modifier = Modifier
                  .clip(RoundedCornerShape(100.dp))
                  .size(60.dp)
                  .fillMaxHeight())

          // Text information
          Column(modifier = Modifier.padding(8.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
                  Text(text = friend.userName, fontWeight = FontWeight.Bold)
                  Row{
                      Text(text = friend.language.resourcesToString(LocalContext.current))
                      Image(
                          painter = when (friend.language) {
                              Language.ENGLISH -> painterResource(R.drawable.english_logo)
                              Language.FRENCH -> painterResource(R.drawable.french_logo)
                              Language.GERMAN -> painterResource(R.drawable.german_logo)
                          },
                          contentDescription = "language logo",
                          modifier = Modifier.size(25.dp, 25.dp)
                      )
                  }
                }
          }
        }
      Row(modifier = Modifier
          .testTag("FriendPrefActivity")
          .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
          // display the user's sport preference
              val prefActivity = friend.prefActivity
              val prefActivityLevel =
                  when (prefActivity) {
                      ActivityType.CLIMBING -> friend.levels.climbingLevel
                      ActivityType.HIKING -> friend.levels.hikingLevel
                      ActivityType.BIKING -> friend.levels.bikingLevel
                  }
              Text(
                  modifier = Modifier.testTag("friendInfo"),
                  text =
                  when (prefActivity) {
                      ActivityType.CLIMBING ->
                          when (prefActivityLevel) {
                              UserLevel.BEGINNER ->
                                  LocalContext.current.getString(R.string.climbing_newcomer)
                              UserLevel.INTERMEDIATE ->
                                  LocalContext.current.getString(R.string.climbing_amateur)
                              UserLevel.ADVANCED ->
                                  LocalContext.current.getString(R.string.climbing_expert)
                          }
                      ActivityType.HIKING ->
                          when (prefActivityLevel) {
                              UserLevel.BEGINNER ->
                                  LocalContext.current.getString(R.string.hiking_newcomer)
                              UserLevel.INTERMEDIATE ->
                                  LocalContext.current.getString(R.string.hiking_amateur)
                              UserLevel.ADVANCED ->
                                  LocalContext.current.getString(R.string.hiking_expert)
                          }
                      ActivityType.BIKING ->
                          when (prefActivityLevel) {
                              UserLevel.BEGINNER ->
                                  LocalContext.current.getString(R.string.biking_newcomer)
                              UserLevel.INTERMEDIATE ->
                                  LocalContext.current.getString(R.string.biking_amateur)
                              UserLevel.ADVANCED ->
                                  LocalContext.current.getString(R.string.biking_expert)
                          }
                  })
              Image(painter = when (prefActivity) {
                    ActivityType.CLIMBING -> painterResource(R.drawable.climbing_roundicon)
                    ActivityType.HIKING -> painterResource(R.drawable.hiking_roundicon)
                    ActivityType.BIKING -> painterResource(R.drawable.biking_roundicon)

              },
                  contentDescription = "Preferred activity logo",
                  modifier = Modifier.size(25.dp,25.dp))
          }
      }
}
