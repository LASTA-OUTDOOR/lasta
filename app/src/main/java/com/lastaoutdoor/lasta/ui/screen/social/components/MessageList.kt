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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@Composable
fun MessageList(viewModel: SocialViewModel = hiltViewModel()) {
  if (!viewModel.messages.isNullOrEmpty()) {
    MessageMissing()
    return
  }
  LazyColumn { items(10) { MessageCard(i = it) } }
}

@Composable
fun MessageCard(i: Int) {
  Card(
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      modifier = Modifier.height(height = 100.dp).fillMaxWidth().padding(8.dp)) {
      Column (modifier = Modifier.padding(8.dp)){
          Row() {
              Icon(
                  Icons.Filled.AccountCircle,
                  contentDescription = "Profile picture",
                  modifier = Modifier.size(30.dp).align(Alignment.CenterVertically)
              )
              Text(text = " Friend $i", modifier = Modifier.align(Alignment.CenterVertically), fontWeight = FontWeight.Bold)
          }
          Text(text = "Hiii, What are you doing tomorrow ?", overflow = TextOverflow.Ellipsis)
      }
  }
}
