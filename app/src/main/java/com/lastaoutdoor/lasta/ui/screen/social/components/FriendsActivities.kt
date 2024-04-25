package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@Composable
fun FriendsActivityList(viewModel: SocialViewModel = hiltViewModel()) {
  val isConnected = viewModel.isConnected.collectAsState()
  when {
    isConnected.value == ConnectionState.OFFLINE -> {
      ConnectionMissing()
    }
    viewModel.latestFriendActivities.isEmpty() -> {
      ActivitiesMissing()
    }
    else -> {
      LazyColumn {
        items(viewModel.latestFriendActivities.size) {
          FriendsActivityCard(viewModel.latestFriendActivities[it])
        }
      }
    }
  }
}

@Composable
fun FriendsActivityCard(activity: ActivitiesDatabaseType) {
  Card(
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      modifier = Modifier.fillMaxWidth().padding(8.dp).testTag("Activity")) {
        Column(modifier = Modifier.padding(8.dp)) {
          Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = LocalContext.current.getString(R.string.activity_title),
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
              Text(
                  text = LocalContext.current.getString(R.string.friend),
                  modifier = Modifier.align(Alignment.CenterVertically))
              Icon(
                  Icons.Filled.AccountCircle,
                  contentDescription = "Profile picture",
                  modifier = Modifier.size(30.dp).align(Alignment.CenterVertically))
            }
          }
          Text(
              text =
                  "Morning run from Lausanne to Yverdon, nothing fancy but I'm really good and fast",
              modifier = Modifier.padding(8.dp),
              overflow = TextOverflow.Ellipsis)
          HorizontalDivider(thickness = 1.dp, color = Color.Gray)
          Row(
              modifier = Modifier.fillMaxWidth().padding(8.dp),
              horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Time : 45minutes")
                Text("Distance : 20km")
              }
        }
      }
}
