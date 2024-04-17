package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabMenu() {

  var state by remember { mutableIntStateOf(0) }
  val titles = listOf("Feed", "Friends", "Message")

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
    0 -> FriendsActivityList()
    1 -> FriendsList() // TODO: appropriate composable
    2 -> MessageList() // TODO: apppropriate composable
  }
}
