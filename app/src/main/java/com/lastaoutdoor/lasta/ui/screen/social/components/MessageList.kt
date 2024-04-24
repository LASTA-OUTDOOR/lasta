package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lastaoutdoor.lasta.data.model.social.ConversationModel
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@Composable
fun MessageList(navController: NavController, viewModel: SocialViewModel = hiltViewModel()) {
  val isConnected = viewModel.isConnected.collectAsState()
  when {
    isConnected.value == ConnectionState.OFFLINE -> {
      ConnectionMissing()
    }
    viewModel.messages.isEmpty() -> {
      if (viewModel.displayFriendPicker) {
        FriendPicker(navController = navController)
      }
      MessageMissing()
    }
    else -> {
      // call the friend picker (displayed when clicking on the email icon)
      if (viewModel.displayFriendPicker) {
        FriendPicker(navController = navController)
      }

      // list of messages
      LazyColumn { items(viewModel.messages.size) { MessageCard(viewModel.messages[it]) } }
    }
  }
}

@Composable
fun MessageCard(message: ConversationModel) {
  Card(
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      modifier = Modifier.height(height = 100.dp).fillMaxWidth().padding(8.dp).testTag("Message")) {
        Column(modifier = Modifier.padding(8.dp)) {
          Row() {
            Icon(
                Icons.Filled.AccountCircle,
                contentDescription = "Profile picture",
                modifier = Modifier.size(30.dp).align(Alignment.CenterVertically))
            Text(
                text = message.lastMessage?.content ?: "",
                modifier = Modifier.align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold)
          }

          Text(text = message.lastMessage?.content ?: "", overflow = TextOverflow.Ellipsis)
        }
      }
}
