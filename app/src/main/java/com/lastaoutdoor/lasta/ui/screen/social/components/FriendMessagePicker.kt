package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.user.UserModel

// Dialog to select a friend to send a message to
@Composable
fun FriendPicker(
    friends: List<UserModel>,
    hideFriendPicker: () -> Unit,
    navigateToConversation: (String) -> Unit
) {
  Dialog(
      onDismissRequest = { hideFriendPicker() },
      properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)) {
        Card(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.6f).padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
          Text(
              text = LocalContext.current.getString(R.string.select_friend),
              style = MaterialTheme.typography.titleLarge,
              textAlign = TextAlign.Center,
              modifier = Modifier.fillMaxWidth().padding(16.dp))

          FriendLazyColumn(friends, hideFriendPicker, navigateToConversation)
        }
      }
}

// List of all friends to select from when sending a message
@Composable
private fun FriendLazyColumn(
    friends: List<UserModel>,
    hideFriendPicker: () -> Unit,
    navigateToConversation: (String) -> Unit
) {
  LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
    items(friends.size) {
      val friend = friends[it]

      Card(
          modifier =
              Modifier.fillMaxWidth().padding(8.dp).clickable {
                hideFriendPicker()
                navigateToConversation(friend.userId)
              },
          colors =
              CardColors(
                  /*Colors from material theme*/
                  MaterialTheme.colorScheme.primary,
                  MaterialTheme.colorScheme.onPrimary,
                  MaterialTheme.colorScheme.surfaceVariant,
                  MaterialTheme.colorScheme.onSurfaceVariant),
          elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
      ) {
        FriendRow(friend)
      }
    }
  }
}

// Displays the friend information in a row (profile picture + name)
@Composable
fun FriendRow(friend: UserModel) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start) {
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
            modifier = Modifier.clip(RoundedCornerShape(100.dp)).size(30.dp).fillMaxHeight())
        Text(
            text = friend.userName ?: "Undefined",
            modifier = Modifier.align(Alignment.CenterVertically).padding(8.dp))
      }
}
