package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.ui.components.SeparatorComponent
import com.lastaoutdoor.lasta.ui.screen.moreinfo.TopBarLogo

// Display a simple page with all the information of a friend
@Composable
fun FriendProfileScreen(friend: UserModel?, onBack: () -> Unit) {
  Column(
      modifier = Modifier.padding(8.dp).fillMaxHeight(),
      horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Header(onBack)
        if (friend == null) {
          return
        }
        ProfilePicture(friend.profilePictureUrl ?: "")
        Text(text = friend.userName ?: "Name Error", style = MaterialTheme.typography.titleLarge)
        SeparatorComponent()
        Text(
            "Bonjour, ceci est ma biographie ! Bonne journée [To be Changed] ",
            modifier = Modifier.fillMaxHeight())
      }
}

// back arrow to go back to the previous screen
@Composable
private fun Header(onBack: () -> Unit) {
  Row(modifier = Modifier.fillMaxWidth()) { TopBarLogo(R.drawable.arrow_back) { onBack.invoke() } }
}

// Display the profile picture of the friend in an AsyncImage
@Composable
private fun ProfilePicture(url: String) {
  AsyncImage(
      model =
          ImageRequest.Builder(LocalContext.current)
              .data(url)
              .crossfade(true)
              .memoryCachePolicy(CachePolicy.ENABLED)
              .build(),
      placeholder = painterResource(R.drawable.default_profile_icon),
      contentDescription = "Profile Picture",
      contentScale = ContentScale.Crop,
      error = painterResource(R.drawable.default_profile_icon),
      modifier =
          Modifier.clip(RoundedCornerShape(100.dp)).size(90.dp).fillMaxHeight().shadow(20.dp))
}
