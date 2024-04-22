package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController

@Composable
fun ConversationScreen(navController: NavController) {

  Text("Conversation Screen", modifier = Modifier.testTag("Header"))

}
