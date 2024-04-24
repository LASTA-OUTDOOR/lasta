package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.navigation.LeafScreen
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabMenu(navController: NavController, viewModel: SocialViewModel = hiltViewModel()) {

  var state by remember { mutableIntStateOf(0) }
  val titles =
      listOf(
          LocalContext.current.getString(R.string.feed),
          LocalContext.current.getString(R.string.message),
          LocalContext.current.getString(R.string.friends))

  Column {
    PrimaryTabRow(selectedTabIndex = state) {
      titles.forEachIndexed { index, title ->
        Tab(
            selected = state == index,
            onClick = { state = index },
            text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) })
      }
    }
  }

  // use the state variable to choose which composable to display
  when (state) {
    0 -> {
      viewModel.hideTopButton()
      FriendsActivityList()
    }
    1 -> {
      viewModel.showTopButton(
          Icons.Filled.Add, onClick = { navController.navigate(LeafScreen.AddFriend.route) })
      FriendsList()
    }
    2 -> {
      viewModel.showTopButton(
          Icons.Filled.Email, onClick = { navController.navigate(LeafScreen.NewMessage.route) })
      MessageList()
    }
  }
}
