package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.screen.moreinfo.TopBarLogo
import com.lastaoutdoor.lasta.ui.screen.profile.ProfileScreen
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.TimeFrame

// Display a simple page with all the information of a friend
@Composable
fun FriendProfileScreen(
    activities: List<UserActivity>,
    timeFrame: TimeFrame,
    sport: ActivityType,
    isCurrentUser: Boolean,
    user: UserModel,
    setSport: (ActivityType) -> Unit,
    setTimeFrame: (TimeFrame) -> Unit,
    navigateToSettings: () -> Unit,
    onBack: () -> Unit,
    isOnline: ConnectionState
) {

  // Display the user's profile
  ProfileScreen(
      activities = activities,
      timeFrame = timeFrame,
      sport = sport,
      isCurrentUser = isCurrentUser,
      user = user,
      updateDescription = {}, // will never be used
      setSport = setSport,
      setTimeFrame = setTimeFrame,
      isOnline = isOnline,
      navigateToSettings = { navigateToSettings() })
  Header(onBack, isOnline)
}

// back arrow to go back to the previous screen
@Composable
private fun Header(onBack: () -> Unit, isOnline: ConnectionState) {
  // display back arrow on blue background
  Row(modifier = Modifier.fillMaxWidth().testTag("FriendProfileHeader")) {
    TopBarLogo(R.drawable.arrow_back, isFriendProf = true, { onBack() }, isOnline)
  }
}
