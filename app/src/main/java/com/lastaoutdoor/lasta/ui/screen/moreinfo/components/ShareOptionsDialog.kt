package com.lastaoutdoor.lasta.ui.screen.moreinfo.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.components.shareActivity

@Composable
fun ShareOptionsDialog(activity: Activity, openDialog: MutableState<Boolean>, friends : List<String>, shareToFriend : (String, String) -> Unit) {
    val context = LocalContext.current
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = LocalContext.current.getString(R.string.share_options), style = MaterialTheme.typography.displayMedium) },
            text = {
                Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = { 
                        FriendSharePicker(friends = , hideFriendPicker = { /*TODO*/ }) {
                            
                        }
                        openDialog.value = false
                    }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                        Text(LocalContext.current.getString(R.string.share_in_app), style = MaterialTheme.typography.bodySmall)
                    }
                    Button(onClick = {
                        shareActivity(activity, context)
                        openDialog.value = false
                    }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                        Text(LocalContext.current.getString(R.string.share_outside), style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text(LocalContext.current.getString(R.string.cancel), style = MaterialTheme.typography.bodyMedium)
                }
            }
        )
    }
}