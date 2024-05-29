package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.FriendsActivities
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.utils.ConnectionState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TabMenu(
    isConnected: ConnectionState,
    friends: List<UserModel>,
    messages: List<ConversationModel>,
    latestFriendActivities: List<FriendsActivities>,
    addFriendDialog: Boolean,
    friendRequestFeedback: String,
    friendSuggestions: List<UserModel>,
    isDisplayedFriendPicker: Boolean,
    refreshMessages: () -> Unit,
    clearFriendRequestFeedback: () -> Unit,
    hideAddFriendDialog: () -> Unit,
    fetchFriendsSuggestions: (String) -> Unit,
    requestFriend: (String) -> Unit,
    refreshFriends: () -> Unit,
    showTopButton: (ImageVector, () -> Unit) -> Unit,
    hideTopButton: () -> Unit,
    displayAddFriendDialog: () -> Unit,
    displayFriendPicker: () -> Unit,
    hideFriendPicker: () -> Unit,
    changeDisplayFriendPicker: () -> Unit,
    navigateToConversation: (String) -> Unit,
    navigateToFriendProfile: (String) -> Unit,
    refreshFriendsActivities: () -> Unit
) {

  val tabs =
    listOf(
        LocalContext.current.getString(R.string.feed),
        LocalContext.current.getString(R.string.friends),
        LocalContext.current.getString(R.string.message))

  //states for the tabs
  var selectedTab by remember { mutableIntStateOf(0) }
  val pagerState = rememberPagerState {
      tabs.size
  }

  //Listen for tab changes (when clicking on a tab)
  LaunchedEffect(selectedTab){
      pagerState.scrollToPage(selectedTab)
  }

  //Listen for swipe changes (when swiping between tabs)
  LaunchedEffect(pagerState.currentPage){
      selectedTab = pagerState.currentPage
  }

  Column {
    PrimaryTabRow(selectedTabIndex = selectedTab) {

      //iterate through the tabs list and create a Tab composable for each tab
      tabs.forEachIndexed { index, title ->
        Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index }, //update selected tab index
            modifier = Modifier.testTag("TabNumber$index"),
            text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) })
      }
    }

    // Pager to be able to swipe between the tabs
    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxHeight().weight(1f), verticalAlignment = Alignment.Top) {
        index ->
        when (index) {
          0 -> {
              hideTopButton()
              refreshFriendsActivities()
              FriendsActivityList(isConnected, latestFriendActivities)
          }
          1 -> {
              showTopButton(Icons.Filled.Add) { displayAddFriendDialog() }
              FriendsList(
                  isConnected,
                  friends,
                  addFriendDialog,
                  friendRequestFeedback,
                  friendSuggestions,
                  clearFriendRequestFeedback,
                  hideAddFriendDialog,
                  fetchFriendsSuggestions,
                  requestFriend,
                  refreshFriends,
                  navigateToFriendProfile)
          }
          2 -> {
              showTopButton(Icons.Filled.Email) { displayFriendPicker() }
              MessageList(
                  isConnected,
                  messages,
                  refreshMessages,
                  friends,
                  hideFriendPicker,
                  navigateToConversation,
                  isDisplayedFriendPicker,
                  changeDisplayFriendPicker)
          }
    }

  }
  }
}

