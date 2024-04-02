package com.lastaoutdoor.lasta.view.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Sport(val name: String)

@Composable
fun Sports(sportsName: String, dropDownItems: List<Sport>, onItemClick: (Sport) -> Unit) {
  var isContextMenuVisible by rememberSaveable { mutableStateOf(false) }

  Button(onClick = { isContextMenuVisible = true }) {
    Box(modifier = Modifier.padding(4.dp)) {
      Text(text = sportsName)
      DropdownMenu(
          expanded = isContextMenuVisible, onDismissRequest = { isContextMenuVisible = false }) {
            dropDownItems.forEach { item ->
              DropdownMenuItem(
                  text = { Text(item.name) },
                  onClick = {
                    onItemClick(item)
                    isContextMenuVisible = false
                  })
            }
          }
    }
  }
}
