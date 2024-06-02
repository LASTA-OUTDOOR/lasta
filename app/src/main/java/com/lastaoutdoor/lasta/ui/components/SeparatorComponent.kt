package com.lastaoutdoor.lasta.ui.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/** A simple composable that draws a horizontal divider with a padding. */
@Composable
fun SeparatorComponent() {
  HorizontalDivider(
      color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
      thickness = 1.dp,
  )
}
