package com.lastaoutdoor.lasta.ui.screen.discovery.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R

@Composable
fun ModalUpperSheet(isRangePopup: Boolean) {
  if (isRangePopup) {
    Surface(
        modifier = Modifier.testTag("modalUpperSheet").height(200.dp),
    ) {
      Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
          // put a small setting icon here with the settings built in icon
          Icon(
              imageVector = Icons.Default.Settings,
              contentDescription = "Settings",
              tint = MaterialTheme.colorScheme.secondary)
          Text(
              text = "   ${LocalContext.current.getString(R.string.modify_search)}   ",
          )
          Icon(
              imageVector = Icons.Default.Settings,
              contentDescription = "Settings",
              tint = MaterialTheme.colorScheme.secondary)
        }
      }
    }
  }
}
