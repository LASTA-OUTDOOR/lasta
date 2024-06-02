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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.ui.theme.Grey
import com.lastaoutdoor.lasta.utils.createColorString
import com.lastaoutdoor.lasta.viewmodel.TrackingState
import java.util.Locale

@Composable
fun TrackingInfo(
    modifier: Modifier = Modifier,
    trackingState: TrackingState,
    hours: String,
    minutes: String,
    seconds: String
) {

  Column(
      modifier = modifier.fillMaxSize().padding(16.dp).testTag("TrackingInfo"),
      verticalArrangement = Arrangement.SpaceBetween) {
        Column(
            modifier = Modifier.weight(0.34f).fillMaxSize(),
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
        InfoRow(
            modifier = Modifier.weight(0.33f),
            first1 = "Distance ",
            second1 = "/ done",
            info1 = "${String.format(Locale.US, "%.1f", trackingState.distanceDone / 1000.0)} Km",
            first2 = "Distance",
            second2 = "/ total",
            info2 = "11.3 Km")
        InfoRow(
            modifier = Modifier.weight(0.33f),
            first1 = "Avg. pace",
            second1 = " min/km",
            info1 = "00:00",
            first2 = "Steps",
            second2 = " / total",
            info2 = trackingState.stepCount.toString())
      }
}

@Composable
fun InfoRow(
    modifier: Modifier = Modifier,
    first1: String,
    second1: String,
    info1: String,
    first2: String,
    second2: String,
    info2: String
) {
  Row(modifier = modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
    InfoColumn(
        modifier = Modifier.weight(0.5f),
        first = first1,
        second = second1,
        info = info1,
        greyColor = Grey)
    InfoColumn(
        modifier = Modifier.weight(0.5f),
        first = first2,
        second = second2,
        info = info2,
        greyColor = Grey)
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
