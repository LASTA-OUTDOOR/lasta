package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lastaoutdoor.lasta.R

// Dialog to select a friend to send a message to
@Composable
fun AddFriendDialog(
    friendRequestFeedback: String,
    clearFriendRequestFeedback: () -> Unit,
    hideAddFriendDialog: () -> Unit,
    requestFriend: (String) -> Unit
) {

  // Reset the feedback message on launched effect
  LaunchedEffect(Unit) { clearFriendRequestFeedback() }

  Dialog(
      onDismissRequest = { hideAddFriendDialog() },
      properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("AddFriendDialog"),
            shape = RoundedCornerShape(16.dp),
        ) {
          Text(
              text = LocalContext.current.getString(R.string.add_fr),
              style = MaterialTheme.typography.titleLarge,
              textAlign = TextAlign.Center,
              modifier = Modifier.fillMaxWidth().padding(16.dp))
          Column(
              modifier = Modifier.padding(16.dp).fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    LocalContext.current.getString(R.string.fr_email_add),
                    modifier = Modifier.testTag("SubHeader"),
                    textAlign = TextAlign.Center)
                AddFriendForm(friendRequestFeedback, requestFriend)
              }
        }
      }
}

@Composable
private fun AddFriendForm(friendRequestFeedback: String, requestFriend: (String) -> Unit) {

  // to hide the keyboard
  val focusManager = LocalFocusManager.current
  // text inside the textfield
  var text by remember { mutableStateOf("") }

  TextField(
      value = text,
      onValueChange = { text = it },
      label = { Text(LocalContext.current.getString(R.string.email)) },
      modifier = Modifier.fillMaxWidth().padding(8.dp).testTag("EmailTextField"),
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
      keyboardActions =
          KeyboardActions(
              onDone = {
                focusManager.clearFocus()
                requestFriend(text)
              }))

  // Error message / Feedback
  Text(friendRequestFeedback, style = MaterialTheme.typography.bodyLarge)

  // Submit Button
  Button(
      onClick = {
        focusManager.clearFocus()
        requestFriend(text)
      },
      modifier = Modifier.testTag("SubmitButton")) {
        Text(LocalContext.current.getString(R.string.send_fr))
      }
}
