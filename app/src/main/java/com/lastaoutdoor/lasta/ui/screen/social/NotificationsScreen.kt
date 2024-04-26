package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.screen.moreinfo.TopBarLogo
import com.lastaoutdoor.lasta.ui.screen.social.components.FriendsRequestList

@Composable
fun NotificationsScreen(navController: NavController) {
  Column {
    // Top Bar
    Row(modifier = Modifier.fillMaxWidth()) {
      TopBarLogo(R.drawable.arrow_back) { navController.navigateUp() }
    }
    FriendsRequestList()
  }
}
