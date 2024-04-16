package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@Composable
fun Header() {
  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    Text(
        text = "Friends",
        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
        modifier = Modifier.align(Alignment.CenterVertically))
    Button(
        onClick = { /* Your button click logic here */},
        modifier = Modifier.align(Alignment.CenterVertically)) {
          Text(text = "+")
        }
  }
}

@Composable
fun SubTitle() {
  Text(
      text = "Your friend's activity",
      style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
      modifier = Modifier.padding(top = 8.dp))
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

@Composable
fun FriendsActivityList(viewModel: SocialViewModel = hiltViewModel()) {
  if (viewModel.friends == null) {
    ConnectionMissing()
    return
  }
  LazyColumn { items(10) { FriendsActivityCard(it) } }
}

@Composable
fun ConnectionMissing() {
  Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.internet_icon),
            contentDescription = "Internet Icon")

        Text(
            text =
                "This content requires an internet connection. Please check your connection and try again.",
            style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
            modifier = Modifier.padding(top = 8.dp))
      }
}

@Composable
fun SocialScreen(viewModel: SocialViewModel = hiltViewModel()) {
  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

    // page title and button
    Header()

    // Sub-title
    SubTitle()

    // Do we have an internet connection?
    // yes: show the list of friends activities
    FriendsActivityList()
    // no: show a message to the user
    // ConnectionMissing()

  }
}

// preview
@Preview
@Composable
fun SocialScreenPreview() {
  SocialScreen()
}
