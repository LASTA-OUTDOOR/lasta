package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lastaoutdoor.lasta.ui.navigation.LeafScreen
import com.lastaoutdoor.lasta.ui.screen.social.components.TabMenu
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@Composable
fun Header(navController: NavController, viewModel: SocialViewModel = hiltViewModel()) {
  Row(
      modifier = Modifier.fillMaxWidth().testTag("Header"),
      horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "Community",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            modifier = Modifier.align(Alignment.CenterVertically),
        )
        if (viewModel.friendButton) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = { viewModel.topButtonOnClick.invoke() },
                modifier = Modifier.align(Alignment.CenterVertically).testTag("TopButton")) {
                  Text(text = viewModel.topButtonText)
                }
            IconButton(onClick = { navController.navigate(LeafScreen.Notifications.route) }) {
              Icon(Icons.Filled.Notifications, contentDescription = "Notification Icon")
            }
          }
        }
        Spacer(
            modifier =
                Modifier.padding(top = 8.dp, bottom = 40.dp).align(Alignment.CenterVertically))
      }
}

@Composable
fun SocialScreen(navController: NavController) {
  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

    // Page title and button
    Header(navController)

    // Tabs
    TabMenu(navController)
  }
}
