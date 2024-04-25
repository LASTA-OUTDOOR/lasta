package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@Composable
fun ConnectionMissing() {
  Column(
      modifier = Modifier.fillMaxSize().testTag("ConnectionMissing"),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.internet_icon),
            contentDescription = "Internet Icon")

        Text(
            text = LocalContext.current.getString(R.string.req_internet),
            style =
                TextStyle(
                    fontWeight = FontWeight.Normal, fontSize = 20.sp, textAlign = TextAlign.Center),
            modifier = Modifier.padding(top = 8.dp))
      }
}

@Composable
fun FriendsMissing() {
  Column(
      modifier = Modifier.fillMaxSize().testTag("FriendMissing"),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = LocalContext.current.getString(R.string.no_friends_yet),
            style =
                TextStyle(
                    fontWeight = FontWeight.Normal, fontSize = 20.sp, textAlign = TextAlign.Center),
            modifier = Modifier.padding(15.dp))
      }
}

@Composable
fun ActivitiesMissing(){
    Column(
        modifier = Modifier.fillMaxSize().testTag("FriendMissing"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = LocalContext.current.getString(R.string.no_friend_activities),
            style =
            TextStyle(
                fontWeight = FontWeight.Normal, fontSize = 20.sp, textAlign = TextAlign.Center),
            modifier = Modifier.padding(15.dp))
    }
}

@Composable
fun MessageMissing(viewModel: SocialViewModel = hiltViewModel()) {
  Column(
      modifier = Modifier.fillMaxSize().testTag("MessageMissing"),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = LocalContext.current.getString(R.string.no_msg),
            style =
                TextStyle(
                    fontWeight = FontWeight.Normal, fontSize = 20.sp, textAlign = TextAlign.Center),
            modifier = Modifier.padding(15.dp))
        Button(onClick = { viewModel.displayFriendPicker() }) {
          Text(text = LocalContext.current.getString(R.string.start_conv))
        }
      }
}
