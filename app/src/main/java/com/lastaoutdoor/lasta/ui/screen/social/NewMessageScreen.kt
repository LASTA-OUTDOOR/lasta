package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController

@Composable
fun NewMessageScreen(navController: NavController) {

  Text("New Message Screen", modifier = Modifier.testTag("Header"))
}
