package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.ui.screen.social.components.FriendsActivityList
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
