package com.lastaoutdoor.lasta.ui.screen.moreinfo.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.ui.components.shareActivity

@Composable
fun ShareOptionsDialog(activity: Activity, openDialog: MutableState<Boolean>) {
    val context = LocalContext.current
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = LocalContext.current.getString(R.string.share_options)) },
            text = {
                Column {
                    TextButton(onClick = {
                        // TODO: Implement in-app share functionality
                        openDialog.value = false
                    }) {
                        Text(LocalContext.current.getString(R.string.share_in_app))
                    }
                    TextButton(onClick = {
                        shareActivity(activity, context)
                        openDialog.value = false
                    }) {
                        Text(LocalContext.current.getString(R.string.share_outside))
                    }
                }
            },
            confirmButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text(LocalContext.current.getString(R.string.cancel))
                }
            }
        )
    }
}