package com.lastaoutdoor.lasta.ui.screen.moreinfo.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.components.shareActivity

/** Dialog to select a friend to share an activity with */
@Composable
fun ShareOptionsDialog(
    activity: Activity,
    openDialog: MutableState<Boolean>,
    friends: List<UserModel>,
    shareToFriend: (String, String) -> Unit
) {
  val context = LocalContext.current
  val showFriendSharePicker = remember { mutableStateOf(false) }

  // Instanciate the friend share picker dialog with the activity to share and the list of friends
  if (showFriendSharePicker.value) {
    FriendSharePicker(activity, friends, { showFriendSharePicker.value = false }, shareToFriend)
  }

  if (openDialog.value) {
    // Dialog to select between sharing the activity in the app or outside
    AlertDialog(
        modifier = Modifier.testTag("shareOptionsDialog").size(250.dp, 250.dp),
        onDismissRequest = { openDialog.value = false },
        title = {
          Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { openDialog.value = false },
                modifier = Modifier.align(Alignment.TopStart).testTag("closeShareOptionsButton")) {
                  Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            Text(
                text = LocalContext.current.getString(R.string.share_options),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center))
          }
        },
        text = {
          Column(
              modifier = Modifier.fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    modifier = Modifier.testTag("shareInAppButton").fillMaxWidth(0.9f),
                    onClick = {
                      showFriendSharePicker.value = true
                      openDialog.value = false
                    },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(20)) {
                      Text(
                          LocalContext.current.getString(R.string.share_in_app),
                          style = MaterialTheme.typography.bodySmall,
                          textAlign = TextAlign.Center)
                    }
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    modifier = Modifier.testTag("shareOutsideButton").fillMaxWidth(0.9f),
                    onClick = {
                      shareActivity(activity, context)
                      openDialog.value = false
                    },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(20)) {
                      Text(
                          LocalContext.current.getString(R.string.share_outside),
                          style = MaterialTheme.typography.bodySmall,
                          textAlign = TextAlign.Center)
                    }
              }
        },
        confirmButton = {})
  }
}
