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

@Composable
fun SendMessageDialog(hideDialog: () -> Unit, send: (String) -> Unit) {
  Dialog(
      onDismissRequest = { hideDialog() },
      properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("SendMessageDialog"),
            shape = RoundedCornerShape(16.dp),
        ) {
          Text(
              text = LocalContext.current.getString(R.string.type_message),
              style = MaterialTheme.typography.titleLarge,
              textAlign = TextAlign.Center,
              modifier = Modifier.fillMaxWidth().padding(16.dp))
          Column(
              modifier = Modifier.padding(16.dp).fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SendMessageForm(send, hideDialog)
              }
        }
      }
}

@Composable
private fun SendMessageForm(send: (String) -> Unit, hideDialog: () -> Unit) {

  // to hide the keyboard
  val focusManager = LocalFocusManager.current
  // text inside the textfield
  var text by remember { mutableStateOf("") }

  TextField(
      value = text,
      onValueChange = { text = it },
      label = { Text(LocalContext.current.getString(R.string.your_message)) },
      modifier = Modifier.fillMaxWidth().padding(8.dp).testTag("SendMessageForm"),
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
      keyboardActions =
          KeyboardActions(
              onDone = {
                focusManager.clearFocus()
                send.invoke(text)
                hideDialog()
              }))

  // Submit Button
  Button(
      onClick = {
        focusManager.clearFocus()
        send.invoke(text)
        hideDialog()
      },
      modifier = Modifier.testTag("SendMessageButton")) {
        Text(LocalContext.current.getString(R.string.send_message))
      }
}
