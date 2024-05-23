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
import com.lastaoutdoor.lasta.ui.theme.Grey
import com.lastaoutdoor.lasta.utils.createColorString
import com.lastaoutdoor.lasta.viewmodel.TrackingState

@Composable
fun TrackingInfo(
    modifier: Modifier = Modifier,
    trackingState: TrackingState,
    hours: String,
    minutes: String,
    seconds: String
) {

  Column(
      modifier = modifier.fillMaxSize().padding(16.dp),
      verticalArrangement = Arrangement.SpaceBetween) {
        Column(
            modifier = Modifier.weight(0.25f).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween) {
              Text(
                  createColorString("Duration ", "/ time", MaterialTheme.colorScheme.primary, Grey),
                  style = MaterialTheme.typography.bodySmall,
                  modifier = Modifier.weight(0.2f))
              Row(
                  modifier = Modifier.weight(0.8f).fillMaxSize(),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically) {
                    Text("$hours:$minutes:$seconds", style = MaterialTheme.typography.displayLarge)
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
                  greyColor = Grey)
              InfoColumn(
                  modifier = Modifier.weight(0.5f),
                  first = "Distance ",
                  second = "/ remaining",
                  info = "2.60 Km",
                  greyColor = Grey)
            }
        Row(
            modifier = Modifier.weight(0.25f).fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween) {
              InfoColumn(
                  modifier = Modifier.weight(0.5f),
                  first = "Avg. pace",
                  second = " min/km",
                  info = "05:49",
                  greyColor = Grey)
              InfoColumn(
                  modifier = Modifier.weight(0.5f),
                  first = "Avg. speed",
                  second = " km/h",
                  info = "10.3",
                  greyColor = Grey)
            }
        Row(
            modifier = Modifier.weight(0.25f).fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween) {
              InfoColumn(
                  modifier = Modifier.weight(0.5f),
                  first = "Steps ",
                  second = "/ total",
                  info = trackingState.stepCount.toString(),
                  greyColor = Grey)
              InfoColumn(
                  modifier = Modifier.weight(0.5f),
                  first = "Elevation",
                  second = " gain",
                  info = "23 m",
                  greyColor = Grey)
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
    Text(
        info,
        modifier =
            Modifier.weight(0.8f).fillMaxSize().padding(vertical = 12.dp, horizontal = 20.dp),
        style = MaterialTheme.typography.displayMedium)
  }
}
