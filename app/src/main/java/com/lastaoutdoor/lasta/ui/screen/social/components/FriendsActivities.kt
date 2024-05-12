package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.social.FriendsActivities
import com.lastaoutdoor.lasta.models.user.BikingUserActivity
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.HikingUserActivity
import com.lastaoutdoor.lasta.ui.components.SeparatorComponent
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.formatDate
import com.lastaoutdoor.lasta.utils.timeFromActivityInMillis

@Composable
fun FriendsActivityList(
    isConnected: ConnectionState,
    latestFriendActivities: List<FriendsActivities>
) {
  when {
    isConnected == ConnectionState.OFFLINE -> {
      ConnectionMissing()
    }
    latestFriendActivities.isEmpty() -> {
      ActivitiesMissing()
    }
    else -> {
      LazyColumn {
        items(latestFriendActivities.size) { FriendsActivityCard(latestFriendActivities[it]) }
      }
    }
  }
}

@Composable
fun FriendsActivityCard(friendActivity: FriendsActivities) {
  Card(
      elevation = CardDefaults.cardElevation(3.dp),
      modifier =
          Modifier.fillMaxWidth()
              .padding(8.dp)
              .testTag("FriendActivityCard${friendActivity.userActivity.activityId}")) {
        Column(modifier = Modifier.padding(8.dp)) {
          Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp).fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween) {
                val activityTitle: String = friendActivity.activity.name
                Text(
                    text = activityTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis)

                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = friendActivity.friend.userName)

                  Spacer(modifier = Modifier.width(8.dp))

                  AsyncImage(
                      model = friendActivity.friend.profilePictureUrl,
                      contentDescription = "friend profile picture",
                      modifier = Modifier.size(40.dp).clip(CircleShape),
                      contentScale = ContentScale.Crop,
                      error = painterResource(id = R.drawable.default_profile_icon))
                }
              }
          Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp)) {
            val activityType: String =
                friendActivity.userActivity.activityType.resourcesToString(LocalContext.current)
            Text(text = activityType, style = MaterialTheme.typography.titleSmall)
          }
          Spacer(modifier = Modifier.height(16.dp))
          SeparatorComponent()

          Row(
              modifier = Modifier.fillMaxWidth().padding(8.dp),
              horizontalArrangement = Arrangement.SpaceBetween) {
                val duration = timeFromActivityInMillis(listOf(friendActivity.userActivity))
                val hours = duration / 3600000
                val minutes = (duration % 3600000) / 60000
                Text("Duration: ${hours}h${minutes}m", style = MaterialTheme.typography.bodyMedium)

                when (friendActivity.userActivity.activityType) {
                  ActivityType.CLIMBING -> {
                    Text(
                        "Elevation: ${(friendActivity.userActivity as ClimbingUserActivity).totalElevation} m",
                        style = MaterialTheme.typography.bodyMedium)
                  }
                  ActivityType.HIKING -> {
                    Text(
                        "Distance: ${(friendActivity.userActivity as HikingUserActivity).distanceDone.toLong() / 1000} km",
                        style = MaterialTheme.typography.bodyMedium)
                  }
                  ActivityType.BIKING -> {
                    Text(
                        "Distance: ${(friendActivity.userActivity as BikingUserActivity).distanceDone.toLong() / 1000} km",
                        style = MaterialTheme.typography.bodyMedium)
                  }
                }
              }
          Row(
              modifier = Modifier.fillMaxWidth().padding(8.dp),
              horizontalArrangement = Arrangement.SpaceBetween) {
                when (friendActivity.userActivity.activityType) {
                  ActivityType.CLIMBING -> {
                    Text(
                        "Pitches: ${(friendActivity.userActivity as ClimbingUserActivity).numPitches}",
                        style = MaterialTheme.typography.bodyMedium)
                  }
                  ActivityType.HIKING -> {
                    Text(
                        "Average speed: ${(friendActivity.userActivity as HikingUserActivity).avgSpeed}",
                        style = MaterialTheme.typography.bodyMedium)
                  }
                  ActivityType.BIKING -> {
                    Text(
                        "Average speed: ${(friendActivity.userActivity as BikingUserActivity).avgSpeed}",
                        style = MaterialTheme.typography.bodyMedium)
                  }
                }

                Text(
                    "Date: ${formatDate(friendActivity.userActivity.timeStarted)}",
                    style = MaterialTheme.typography.bodyMedium)
              }
        }
      }
}
