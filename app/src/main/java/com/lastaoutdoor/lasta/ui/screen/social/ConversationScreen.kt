package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.components.SeparatorComponent
import com.lastaoutdoor.lasta.ui.screen.moreinfo.TopBarLogo
import com.lastaoutdoor.lasta.ui.screen.social.components.ProfilePicture

@Composable
fun ConversationScreen(
    conversationModel: ConversationModel?,
    refresh: () -> Unit,
    user: UserModel,
    friend: UserModel,
    send: (String) -> Unit,
    navigateBack: () -> Unit
) {
  Column {
    if (conversationModel == null || conversationModel.members.isEmpty()) {
      refresh.invoke()
      return
    }

    Header(navigateBack, friend)
    SeparatorComponent()

    // focus manger
    val focusManger = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize().testTag("ConversationScreen"),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
          LazyColumn(
              modifier =
                  Modifier.fillMaxWidth().fillMaxSize(0.8f).pointerInput(Unit) {
                    detectTapGestures(onPress = { focusManger.clearFocus() })
                  },
              reverseLayout = true) {
                // display 10 Text for testing
                items(conversationModel.messages.size) {
                  // see this function maybe can be wrong
                  ShowMessage(conversationModel.messages[it], user)
                }
              }

          // horizontal bar
          SeparatorComponent()

          // Icon to display the send message button
          Row(
              modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically) {

                // textfield value
                var message by remember { mutableStateOf("") }

                // Message textfield
                TextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier.fillMaxWidth(0.8f).testTag("MessageTextField"))

                // Send message button
                IconButton(
                    onClick = {
                      focusManger.clearFocus()
                      send.invoke(message)
                      message = ""
                    },
                    colors =
                        IconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContentColor =
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
                            disabledContainerColor =
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)),
                    modifier =
                        Modifier.padding(8.dp)
                            .align(Alignment.CenterVertically)
                            .testTag("SendMessageButton")) {
                      Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send message")
                    }
              }
        }
  }
}

// Top bar of the conversation screen
@Composable
fun Header(backCallBack: () -> Unit, friend: UserModel) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("ConversationScreenHeader"),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically) {
        // back button
        TopBarLogo(R.drawable.arrow_back) { backCallBack() }
        Spacer(modifier = Modifier.padding(20.dp))
        ProfilePicture(friend.profilePictureUrl ?: "")
        Spacer(modifier = Modifier.padding(8.dp))
        Text(friend.userName)
      }
}

@Composable
fun ShowMessage(message: MessageModel, user: UserModel) {

  var arrangement = Arrangement.Start
  var backgroundColor = MaterialTheme.colorScheme.surfaceContainer
  var textColor = MaterialTheme.colorScheme.onSurface

  if (message.from == user) {
    arrangement = Arrangement.End
    backgroundColor = MaterialTheme.colorScheme.primary
    textColor = MaterialTheme.colorScheme.onPrimary
  }

  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = arrangement) {
    Card(
        modifier =
            Modifier.padding(8.dp)
                .widthIn(0.dp, LocalConfiguration.current.screenWidthDp.dp * 0.55f),
        colors =
            CardColors(
                containerColor = backgroundColor,
                contentColor = textColor,
                disabledContentColor = backgroundColor,
                disabledContainerColor = textColor,
            )) {
          Text(message.content, modifier = Modifier.padding(8.dp))
        }
  }
}
