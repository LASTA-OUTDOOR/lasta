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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R

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
fun FriendsMissing() {
  Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "We didn't find any activities performed by your friends lately...",
            style =
                TextStyle(
                    fontWeight = FontWeight.Normal, fontSize = 20.sp, textAlign = TextAlign.Center),
            modifier = Modifier.padding(15.dp))
        Button(onClick = { /*TODO*/}) { Text(text = "Try adding more friends!") }
      }
}
