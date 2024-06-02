package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.screen.moreinfo.TopBarLogo
import com.lastaoutdoor.lasta.ui.screen.social.components.FriendsRequestList
import com.lastaoutdoor.lasta.utils.ConnectionState

@Composable
fun NotificationsScreen(
    isConnected: ConnectionState,
    friendRequests: List<UserModel>,
    acceptFriend: (UserModel) -> Unit,
    declineFriend: (UserModel) -> Unit,
    navigateBack: () -> Unit
) {
  Column {
    // Top Bar
    Row(modifier = Modifier.fillMaxWidth().testTag("NotificationsScreen")) {
      TopBarLogo(R.drawable.arrow_back, false, { navigateBack() }, ConnectionState.CONNECTED)
    }
    FriendsRequestList(isConnected, friendRequests, acceptFriend, declineFriend)
  }
}
