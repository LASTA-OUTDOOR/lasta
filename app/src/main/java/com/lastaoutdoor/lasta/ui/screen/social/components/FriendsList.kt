package com.lastaoutdoor.lasta.ui.screen.social.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FriendsList(viewModel: SocialViewModel = hiltViewModel()) {

  LaunchedEffect(Unit) { viewModel.refreshFriends() }

  val isConnected = viewModel.isConnected.collectAsState()
  when {
    isConnected.value == ConnectionState.OFFLINE -> {
      ConnectionMissing()
    }
    viewModel.friends.isEmpty() -> {
      FriendsMissing()
    }
    else -> {
      LazyColumn { items(viewModel.friends.size) { FriendsCard(viewModel.friends[it]) } }
    }
  }
}

@Composable
fun FriendsCard(friend: UserModel) {
  Card(
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      modifier = Modifier.height(height = 100.dp).fillMaxWidth().padding(8.dp).testTag("Friend")) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
          Icon(
              Icons.Filled.AccountCircle,
              contentDescription = "Profile picture",
              modifier = Modifier.size(60.dp).align(Alignment.CenterVertically).fillMaxHeight())
          Column(modifier = Modifier.padding(8.dp)) {
            Text(text = friend.userName ?: "Name error", fontWeight = FontWeight.Bold)
            Text(text = "TO BE IMPLEMENTED - User status", overflow = TextOverflow.Ellipsis)
          }
        }
      }
}
