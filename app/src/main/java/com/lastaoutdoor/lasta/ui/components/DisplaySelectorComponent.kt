package com.lastaoutdoor.lasta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun <T> DisplaySelection(
    items: List<T>, // Pass a list of items
    selectedItem: T, // Current selected item
    onSelected: (T) -> Unit, // Lambda to handle item selection
    itemDisplayName: (T) -> String // Lambda to get the display name of the item
) {
  val shape = RoundedCornerShape(20.dp)
  val borderModifier =
      Modifier.padding(4.dp)
          .border(width = 1.dp, color = MaterialTheme.colorScheme.onBackground, shape = shape)

  Row(
      modifier =
          Modifier.clip(shape)
              .background(MaterialTheme.colorScheme.background)
              .padding(1.dp) // Padding for the border effect
              .then(borderModifier)) {
        items.forEachIndexed { index, item ->
          // Determine background and text color based on selection
          val backgroundColor =
              if (selectedItem == item) MaterialTheme.colorScheme.primary else Color.Transparent
          val textColor =
              if (selectedItem == item) MaterialTheme.colorScheme.onPrimary
              else MaterialTheme.colorScheme.onBackground

          Button(
              onClick = { onSelected(item) }, // Use the passed lambda for selection
              colors =
                  ButtonDefaults.buttonColors(
                      containerColor = backgroundColor, contentColor = textColor),
              shape = shape,
              modifier =
                  Modifier.testTag("SelectionItem$index")
                      .height(40.dp)
                      .defaultMinSize(minWidth = 40.dp) // Minimum width for all buttons
              ) {
                Text(
                    text = itemDisplayName(item), // Use the lambda to get item's display name
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium,
                )
              }
        }
      }
}
