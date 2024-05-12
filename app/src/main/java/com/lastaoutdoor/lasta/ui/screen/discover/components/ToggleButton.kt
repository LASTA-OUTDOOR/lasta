package com.lastaoutdoor.lasta.ui.screen.discover.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

@Composable
fun ToggleButton(text: String, onClick: () -> Unit) {
  var isSelected by remember { mutableStateOf(false) }
  val backgroundColor =
      if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
  val contentColor =
      if (isSelected) MaterialTheme.colorScheme.onPrimary
      else MaterialTheme.colorScheme.onBackground

  Button(
      onClick = {
        isSelected = !isSelected
        onClick()
      },
      colors =
          ButtonDefaults.buttonColors(
              containerColor = backgroundColor, contentColor = contentColor),
      shape = MaterialTheme.shapes.small,
      elevation = ButtonDefaults.elevatedButtonElevation(3.dp)) {
        Text(text = text)
      }
}
