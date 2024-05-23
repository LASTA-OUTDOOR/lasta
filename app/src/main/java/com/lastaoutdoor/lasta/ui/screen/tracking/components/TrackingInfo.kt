package com.lastaoutdoor.lasta.ui.screen.tracking.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.utils.createColorString

@Composable
fun TrackingInfo(modifier: Modifier = Modifier) {

  val greyColor = Color(0xFFB6B6B6)

  Column(
      modifier = modifier.fillMaxSize().padding(16.dp),
      verticalArrangement = Arrangement.SpaceBetween) {
        Column(
            modifier = Modifier.weight(0.25f).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween) {
              Text(
                  createColorString(
                      "Duration ", "/ time", MaterialTheme.colorScheme.primary, greyColor),
                  style = MaterialTheme.typography.bodySmall,
                  modifier = Modifier.weight(0.2f))
              Row(
                  modifier = Modifier.weight(0.8f).fillMaxSize(),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically) {
                    Text("00:16:54", style = MaterialTheme.typography.displayLarge)
                  }
            }
        Row(
            modifier = Modifier.weight(0.25f).fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween) {
              InfoColumn(
                  modifier = Modifier.weight(0.5f),
                  first = "Distance ",
                  second = "/ done",
                  info = "3.40 Km",
                  greyColor = greyColor)
              InfoColumn(
                  modifier = Modifier.weight(0.5f),
                  first = "Distance ",
                  second = "/ remaining",
                  info = "2.60 Km",
                  greyColor = greyColor)
            }
        Row(
            modifier = Modifier.weight(0.25f).fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween) {
              InfoColumn(
                  modifier = Modifier.weight(0.5f),
                  first = "Avg. pace",
                  second = " min/km",
                  info = "5:49",
                  greyColor = greyColor)
              InfoColumn(
                  modifier = Modifier.weight(0.5f),
                  first = "Avg. speed",
                  second = " km/h",
                  info = "10.3",
                  greyColor = greyColor)
            }
        Row(
            modifier = Modifier.weight(0.25f).fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween) {
              InfoColumn(
                  modifier = Modifier.weight(0.5f),
                  first = "Steps ",
                  second = "/ total",
                  info = "2135",
                  greyColor = greyColor)
              InfoColumn(
                  modifier = Modifier.weight(0.5f),
                  first = "Elevation",
                  second = " gain",
                  info = "23 m",
                  greyColor = greyColor)
            }
      }
}

@Composable
fun InfoColumn(
    modifier: Modifier = Modifier,
    first: String,
    second: String,
    info: String,
    greyColor: Color
) {
  Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
    Text(
        createColorString(first, second, MaterialTheme.colorScheme.primary, greyColor),
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.weight(0.2f))
    Row(
        modifier = Modifier.weight(0.8f).fillMaxSize().padding(top = 12.dp),
        horizontalArrangement = Arrangement.Center) {
          Text(info, style = MaterialTheme.typography.displayMedium)
        }
  }
}
