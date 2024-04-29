package com.lastaoutdoor.lasta.ui.screen.social.components

import android.annotation.SuppressLint
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.ui.navigation.LeafScreen
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FriendsList(navController: NavController, viewModel: SocialViewModel = hiltViewModel()) {

  LaunchedEffect(Unit) { viewModel.refreshFriends() }

  val isConnected = viewModel.isConnected.collectAsState()
  when {
    isConnected.value == ConnectionState.OFFLINE -> {
      ConnectionMissing()
    }
    viewModel.friends.isEmpty() -> {
      // add friend dialog when you click on the add friend button
      if (viewModel.displayAddFriendDialog) AddFriendDialog()
      FriendsMissing()
    }
    else -> {
      // add friend dialog when you click on the add friend button
      if (viewModel.displayAddFriendDialog) AddFriendDialog()
      LazyColumn {
        items(viewModel.friends.size) {
          FriendsCard(viewModel.friends[it]) {
            navController.navigate(
                LeafScreen.FriendProfile.route + "/${viewModel.friends[it].userId}")
          }
        }
      }
    }
  }
}

@Composable
private fun FriendsCard(friend: UserModel, navToFriend: () -> Unit) {
  Card(
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      modifier =
      Modifier
          .height(height = 100.dp)
          .fillMaxWidth()
          .padding(8.dp)
          .testTag("Friend")
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
            Text(text = friend.userName ?: "Name error", fontWeight = FontWeight.Bold)
              //little easteregg
              if (friend.userName == "Jérémy Doffey" || friend.userName == "Thimphou") {Text(text = "scrum loser")}
              //display the user's sport preference
              Row {
                  Text(text = friend.userLevel.toString())
                  //add prefered sport after refactor

              }

          }
        }
      }
}
