package com.lastaoutdoor.lasta.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun <T> Spinner(
    items: List<T>, // Items to display in the dropdown
    selectedItem: T, // The currently selected item from the ViewModel
    onItemSelected: (T) -> Unit, // Callback to invoke when an item is selected
    fieldText: String
) {
  var expanded by remember { mutableStateOf(false) }

  Column {
    Row {
      Text(fieldText)
      Icon(
          imageVector = Icons.Filled.ArrowDropDown,
          contentDescription = "Dropdown",
          modifier = Modifier.clickable(onClick = { expanded = true }))
    }

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
    Text(selectedItem.toString(), fontWeight = FontWeight.Bold, style = TextStyle(fontSize = 24.sp))
  }
}
