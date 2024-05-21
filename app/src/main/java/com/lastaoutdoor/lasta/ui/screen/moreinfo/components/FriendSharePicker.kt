package com.lastaoutdoor.lasta.ui.screen.moreinfo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.screen.social.components.FriendRow

// Dialog to select a friend to share an activity with
@Composable
fun FriendSharePicker(
    activityShared: Activity,
    friends: List<UserModel>,
    hideFriendPicker: () -> Unit,
    shareToFriend: (String, String) -> Unit
) {
    Dialog(
        onDismissRequest = { hideFriendPicker() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)) {
        Card(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.6f).padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = LocalContext.current.getString(R.string.select_friend),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(16.dp))

            FriendLazyColumn(activityShared, friends, hideFriendPicker, shareToFriend)
        }
    }
}

// List of all friends to select from when sharing an activity
@Composable
private fun FriendLazyColumn(
    activityShared : Activity,
    friends: List<UserModel>,
    hideFriendPicker: () -> Unit,
    shareToFriend: (String, String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(friends.size) {
            val friend = friends[it]

            Card(
                modifier =
                Modifier.fillMaxWidth().padding(8.dp).clickable {
                    hideFriendPicker()
                    shareToFriend("activity", friend.userId)
                },
                colors =
                CardColors(
                    /*Colors from material theme*/
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.colorScheme.onSurfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                FriendRow(friend)
            }
        }
    }
}

