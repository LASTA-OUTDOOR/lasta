package com.lastaoutdoor.lasta.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R

@Composable
fun MapScreen() {
  Column {
    Text("Map Screen")
    Image(
        painter = painterResource(id = R.drawable.pov_img),
        contentDescription = null,
        modifier = Modifier.padding(1.dp).width(300.dp).height(300.dp))
    Text("Pov : tu es en retard au standup meeting")
  }
}
