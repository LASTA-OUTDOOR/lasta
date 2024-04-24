package com.lastaoutdoor.lasta.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight

@Composable
fun <T> DropDownMenuComponent(
    items: List<T>, // Items to display in the dropdown
    selectedItem: T, // The currently selected item from the ViewModel
    onItemSelected: (T) -> Unit, // Callback to invoke when an item is selected
    fieldText: String
) {
  var expanded by remember { mutableStateOf(false) }

  Column {
    Row {
      Text(
          fieldText,
          style = MaterialTheme.typography.headlineMedium,
          color = MaterialTheme.colorScheme.onBackground)
      Icon(
          imageVector = Icons.Outlined.KeyboardArrowDown,
          contentDescription = "Dropdown",
          modifier = Modifier.clickable(onClick = { expanded = true }).testTag("spinnerIcon"),
          tint = MaterialTheme.colorScheme.onBackground)
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      items.forEachIndexed { index, label ->
        DropdownMenuItem(
            modifier = Modifier.testTag("DropdownItem$index"),
            text = { Text(label.toString()) },
            onClick = {
              onItemSelected(label)
              expanded = false
            })
      }
    }
    Text(
        selectedItem.toString(),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground)
  }
}
