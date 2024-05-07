package com.lastaoutdoor.lasta.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays a dropdown menu component.
 *
 * @param T The type of the items.
 * @param items The list of items to display in the dropdown.
 * @param selectedItem The currently selected item.
 * @param onItemSelected A lambda function that handles the selection of an item.
 * @param toStr A lambda function that returns the string representation of an item.
 * @param fieldText The text to display in the field.
 */
@Composable
fun <T> DropDownMenuComponent(
    items: List<T>, // Items to display in the dropdown
    selectedItem: T, // The currently selected item from the ViewModel
    onItemSelected: (T) -> Unit, // Callback to invoke when an item is selected
    toStr: @Composable (T) -> String,
    fieldText: String,
    modifier: Modifier = Modifier
) {
  var expanded by remember { mutableStateOf(false) }


    Row(modifier = modifier.clickable(onClick = { expanded = true }).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
      Text(
          "$fieldText:",
          style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.width(8.dp))
        Column{
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(
                    toStr(selectedItem),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary)

                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier.clickable(onClick = { expanded = true }).testTag("spinnerIcon"),
                    tint = MaterialTheme.colorScheme.primary)
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                items.forEachIndexed { index, label ->
                    DropdownMenuItem(
                        modifier = Modifier.testTag("DropdownItem$index").wrapContentWidth(),
                        text = { Text(toStr(label)) },
                        onClick = {
                            onItemSelected(label)
                            expanded = false
                        })
                }
        }

    }
  }
}
