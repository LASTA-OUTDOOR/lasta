package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@Composable
fun FriendsActivityList(viewModel: SocialViewModel = hiltViewModel()) {
  if (viewModel.latestFriendActivities.isNullOrEmpty()) {
    FriendsMissing()
    return
  }
  LazyColumn { items(10) { FriendsActivityCard(it) } }
}

@Composable
fun FriendsActivityCard(i: Int) {
  Card(
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      modifier = Modifier.height(height = 100.dp).fillMaxWidth().padding(8.dp)) {
        Row {
          Icon(
              Icons.Filled.AccountCircle,
              contentDescription = "Profile picture",
              modifier = Modifier.padding(8.dp).size(30.dp).align(Alignment.CenterVertically))
          Text(text = "Friend $i", modifier = Modifier.align(Alignment.CenterVertically))
        }
        Text(text = "Hiked 200km and went for a MC Donalds", modifier = Modifier.padding(8.dp))
      }
}
