package com.lastaoutdoor.lasta.ui.screen.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun <T> Spinner(
    items: List<T>, // Items to display in the dropdown
    selectedItem: T, // The currently selected item from the ViewModel
    onItemSelected: (T) -> Unit // Callback to invoke when an item is selected
) {
  var expanded by remember { mutableStateOf(false) }

  Box(modifier = Modifier.fillMaxWidth()) {
    Text(
        text = selectedItem.toString(),
        modifier = Modifier.fillMaxWidth().clickable(onClick = { expanded = true }))
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      items.forEach { label ->
        DropdownMenuItem(
            text = { Text(label.toString()) },
            onClick = {
              onItemSelected(label)
              expanded = false
            })
      }
    }
  }
}
