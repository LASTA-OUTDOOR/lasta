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
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.FriendsActivities
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.screen.social.components.TabMenu
import com.lastaoutdoor.lasta.utils.ConnectionState

@Composable
fun SocialScreen(
    hasFriendRequests: Boolean,
    topButton: Boolean,
    topButtonIcon: ImageVector,
    topButtonOnClick: () -> Unit,
    refreshFriendRequests: () -> Unit,
    navigateToNotifications: () -> Unit,
    isConnected: ConnectionState,
    friends: List<UserModel>,
    messages: List<ConversationModel>,
    latestFriendActivities: List<FriendsActivities>,
    addFriendDialog: Boolean,
    friendRequestFeedback: String,
    isDisplayedFriendPicker: Boolean,
    refreshMessages: () -> Unit,
    clearFriendRequestFeedback: () -> Unit,
    hideAddFriendDialog: () -> Unit,
    fetchFriendsSuggestions: (String) -> List<UserModel>,
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
  Column(modifier = Modifier.fillMaxSize().padding(16.dp).testTag("SocialScreen")) {

    // Page title and button
    Header(
        hasFriendRequests,
        topButton,
        topButtonIcon,
        topButtonOnClick,
        refreshFriendRequests,
        refreshFriends,
        navigateToNotifications)

    // Tabs
    TabMenu(
        isConnected,
        friends,
        messages,
        latestFriendActivities,
        addFriendDialog,
        friendRequestFeedback,
        isDisplayedFriendPicker,
        refreshMessages,
        clearFriendRequestFeedback,
        hideAddFriendDialog,
        fetchFriendsSuggestions,
        requestFriend,
        refreshFriends,
        showTopButton,
        hideTopButton,
        displayAddFriendDialog,
        displayFriendPicker,
        hideFriendPicker,
        changeDisplayFriendPicker,
        navigateToConversation,
        navigateToFriendProfile,
        refreshFriendsActivities)
  }
}

@Composable
fun Header(
    hasFriendRequests: Boolean,
    topButton: Boolean,
    topButtonIcon: ImageVector,
    topButtonOnClick: () -> Unit,
    refreshFriendRequests: () -> Unit,
    refreshFriends: () -> Unit,
    navigateToNotifications: () -> Unit
) {

  // This will be called when the composable becomes visible
  LaunchedEffect(Unit) {
    refreshFriendRequests()
    refreshFriends()
  }

  Row(
      modifier = Modifier.fillMaxWidth().testTag("SocialScreenHeader"),
      horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = LocalContext.current.getString(R.string.community),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            modifier = Modifier.align(Alignment.CenterVertically),
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
          if (topButton) {
            IconButton(
                onClick = { topButtonOnClick.invoke() },
                modifier = Modifier.align(Alignment.CenterVertically).testTag("TopButton")) {
                  Icon(topButtonIcon, contentDescription = "Top Action Button")
                }
          }
          BadgedBox(badge = { if (hasFriendRequests) Badge {} }) {
            IconButton(onClick = { navigateToNotifications() }) {
              Icon(Icons.Filled.Notifications, contentDescription = "Notification Icon")
            }
          }
        }
        Spacer(
            modifier =
                Modifier.padding(top = 8.dp, bottom = 40.dp).align(Alignment.CenterVertically))
      }
}
