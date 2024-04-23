package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.screen.activities.TopBarLogo
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@Composable
fun AddFriendScreen(navController: NavController, viewmodel: SocialViewModel = hiltViewModel()) {
  Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    val focusManager = LocalFocusManager.current

    // Top Bar
    Row(modifier = Modifier.fillMaxWidth()) {
      TopBarLogo(R.drawable.arrow_back) { navController.navigateUp() }
    }

    // Form
    Text(
        "Add a friend",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.testTag("Header"))
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
              "Enter a friend's email address to send a friend request",
              modifier = Modifier.testTag("SubHeader"))
          var text by remember { mutableStateOf("") }
          TextField(
              value = text,
              onValueChange = { text = it },
              label = { Text("Email address") },
              modifier = Modifier.fillMaxWidth().padding(8.dp).testTag("EmailTextField"),
              keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
              keyboardActions =
                  KeyboardActions(
                      onDone = {
                        focusManager.clearFocus()
                        viewmodel.requestFriend(text)
                      }))

          // Submit Button
          Button(
              onClick = {
                focusManager.clearFocus()
                viewmodel.requestFriend(text)
              },
              modifier = Modifier.testTag("SubmitButton")) {
                Text("Send friend request")
              }

          // Error message / Feedback
          Text(viewmodel.friendRequestFeedback, style = MaterialTheme.typography.bodyLarge)
        }
  }
}
