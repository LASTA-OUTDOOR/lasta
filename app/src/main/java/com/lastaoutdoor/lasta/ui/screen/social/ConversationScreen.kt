package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.social.ConversationModel
import com.lastaoutdoor.lasta.data.model.social.MessageModel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.ui.components.SeparatorComponent
import com.lastaoutdoor.lasta.ui.screen.activities.TopBarLogo

@Composable
fun ConversationScreen(
    navController: NavController,
    conversationModel: ConversationModel?,
    refresh: () -> Unit,
    userId: String,
    friendId: String,
    showSendDialog: () -> Unit
) {
  println(conversationModel)
  Column {
    if (conversationModel == null || conversationModel.users.isEmpty()) {
      refresh.invoke()
      return
    }

    val friend: UserModel? = conversationModel.users.firstOrNull { it.userId == friendId }
    if (friend == null) {
      return
    }

    Header(navController::popBackStack, friend.userName ?: "Friend")
    SeparatorComponent()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
          LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxSize(0.8f)) {
            // display 10 Text for testing
            items(conversationModel.messages.size) {
              ShowMessage(conversationModel.messages[it], userId)
            }
          }

          // horizontal bar
          SeparatorComponent()

          // Icon to display the send message button
          Row(
              modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(8.dp),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { showSendDialog.invoke() },
                    colors =
                        IconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContentColor =
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
                            disabledContainerColor =
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)),
                    modifier = Modifier.fillMaxWidth(0.6f)) {
                      Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send message")
                    }
              }
        }
  }
}

// Top bar of the conversation screen
@Composable
fun Header(backCallBack: () -> Unit, friendName: String) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically) {
        // back button
        TopBarLogo(R.drawable.arrow_back) { backCallBack() }
        // friend name
        Text(friendName)
      }
}

@Composable
fun ShowMessage(message: MessageModel, userId: String) {

  val arrangement = if (message.from == userId) Arrangement.End else Arrangement.Start

  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = arrangement) {
    Card(modifier = Modifier.padding(8.dp)) {
      Text(message.content, modifier = Modifier.padding(8.dp))
    }
  }
}
