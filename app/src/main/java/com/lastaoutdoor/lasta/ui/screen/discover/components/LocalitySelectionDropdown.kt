package com.lastaoutdoor.lasta.ui.screen.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.ui.theme.AccentGreen

// Dropdown to select the city
@Composable
fun LocalitySelectionDropdown(
    localities: List<Pair<String, LatLng>>,
    selectedLocality: Pair<String, LatLng>,
    setSelectedLocality: (Pair<String, LatLng>) -> Unit
) {
  var expanded by remember { mutableStateOf(false) }

  Box(modifier = Modifier.wrapContentSize().testTag("localitySelectionDropdown")) {
    Text(
        selectedLocality.first,
        modifier =
            Modifier.clickable(onClick = { expanded = true })
                .testTag("localitySelectionDropdownButton"),
        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight(400), color = AccentGreen))
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      localities.forEach { locality ->
        DropdownMenuItem(
            modifier = Modifier.testTag("localitySelectionDropdownItem"),
            text = { Text(locality.first, color = AccentGreen) },
            onClick = {
              setSelectedLocality(locality)
              expanded = false
            })
      }
    }
  }
  Spacer(modifier = Modifier.height(8.dp))
}
