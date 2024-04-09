package com.lastaoutdoor.lasta.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.data.db.Trail
import com.lastaoutdoor.lasta.data.model.Sports
import com.lastaoutdoor.lasta.data.model.user_profile.DaysInWeek
import com.lastaoutdoor.lasta.data.model.user_profile.MonthsInYear
import com.lastaoutdoor.lasta.data.model.user_profile.TimeFrame
import com.lastaoutdoor.lasta.data.model.user_profile.WeeksInMonth
import com.lastaoutdoor.lasta.data.model.user_profile.Year
import com.lastaoutdoor.lasta.ui.components.BarGraph
import com.lastaoutdoor.lasta.ui.components.BarType
import com.lastaoutdoor.lasta.ui.components.Spinner
import com.lastaoutdoor.lasta.utils.caloriesFromHiking
import com.lastaoutdoor.lasta.utils.chartDisplayValues
import com.lastaoutdoor.lasta.utils.formatDate
import com.lastaoutdoor.lasta.utils.metersToKilometers
import com.lastaoutdoor.lasta.utils.timeFromHikingInMillis
import com.lastaoutdoor.lasta.utils.timeFromMillis
import com.lastaoutdoor.lasta.viewmodel.ProfileScreenVIewModel
import java.util.Calendar

@Composable
fun ProfileScreen2(
    profileScreenVIewModel: ProfileScreenVIewModel = hiltViewModel(),
) {
  // profileScreenVIewModel.addTrailToUserActivities()
  val trailList by profileScreenVIewModel.trails.collectAsState()

  LazyColumn(modifier = Modifier) {
    item { SportSelection(profileScreenVIewModel) }
    item { TimeFrameSelection(profileScreenVIewModel) }
    item { Chart(trailList, profileScreenVIewModel) }
    item { RecentActivities(profileScreenVIewModel) }
  }
}

@Composable
fun SportSelection(profileScreenVIewModel: ProfileScreenVIewModel) {
  Row {
    Text("Activity")

    // Sample data for the Spinner
    val spinnerItems = Sports.values().toList()
    // Observe LiveData and convert to Composable State
    // profileScreenVIewModel.addTrailToUserActivities()
    // Now trailListState is a normal List<Trail> that you can use in Compose

    Spinner(
        items = spinnerItems,
        selectedItem = profileScreenVIewModel.getSport(),
        onItemSelected = { newSport -> profileScreenVIewModel.setSport(newSport) })
  }
}

@Composable
fun TimeFrameSelection(profileScreenVIewModel: ProfileScreenVIewModel) {
  Row(modifier = Modifier.padding(8.dp)) {
    val timeFrame by profileScreenVIewModel.timeFrame.collectAsState()
    TimeFrame.values().forEach { timeframe ->
      Button(
          onClick = { profileScreenVIewModel.setTimeFrame(timeframe) },
          colors =
              ButtonDefaults.buttonColors(
                  containerColor = if (timeFrame == timeframe) Color.Gray else Color.LightGray,
                  contentColor = Color.Black),
          shape = RoundedCornerShape(4.dp),
          modifier = Modifier.padding(4.dp)) {
            Text(timeframe.name, color = if (timeFrame == timeframe) Color.White else Color.Black)
          }
    }
  }
}

@Composable
fun Chart(trails: List<Trail>, profileScreenVIewModel: ProfileScreenVIewModel) {
  Column(
      modifier = Modifier.padding(horizontal = 30.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {

        // Bar graph x and y data
        val timeFrame by profileScreenVIewModel.timeFrame.collectAsState()

        // Based on the collected timeFrame, adapt the chart dynamically
        val (values, abscissa) =
            when (timeFrame) {
              TimeFrame.W ->
                  Pair(chartDisplayValues(trails, timeFrame), DaysInWeek.values().toList())
              TimeFrame.M ->
                  Pair(chartDisplayValues(trails, timeFrame), WeeksInMonth.values().toList())
              TimeFrame.Y ->
                  Pair(chartDisplayValues(trails, timeFrame), MonthsInYear.values().toList())
              TimeFrame.ALL -> {
                val start =
                    Calendar.getInstance().apply { time = trails[0].timeStarted }.get(Calendar.YEAR)
                val end =
                    Calendar.getInstance()
                        .apply { time = trails[trails.size - 1].timeStarted }
                        .get(Calendar.YEAR)
                val years = mutableListOf<Year>()
                for (i in start..end) {
                  years.add(Year(i))
                }
                Pair(chartDisplayValues(trails, timeFrame), years.toList())
              }
            }

        val ordinate = values.map { it.toInt() }

        val ordinateFloat: List<Float> =
            values.map {
              val max = ordinate.max()
              if (max == 0) 0f else it / ordinate.max()
            }

        Row {
          Column {
            Text(text = String.format("%.2f", values.sum()))
            Text("Km")
          }
        }

        Row {
          Column {
            Text(trails.size.toString())
            Text("runs")
          }

          Column {
            Text(text = caloriesFromHiking(trails).toString())
            Text("Calories")
          }

          Column {
            Text(text = timeFromMillis(timeFromHikingInMillis(*trails.toTypedArray())))
            Text("Time")
          }
        }

        BarGraph(
            graphBarData = ordinateFloat,
            xAxisScaleData = abscissa,
            barData = ordinate,
            height = 300.dp,
            roundType = BarType.TOP_CURVED,
            barWidth = 20.dp,
            barColor = MaterialTheme.colorScheme.primary,
            barArrangement = Arrangement.SpaceEvenly)
      }
}

@Composable
fun RecentActivities(profileScreenVIewModel: ProfileScreenVIewModel) {
  val activities by profileScreenVIewModel.allTrails.collectAsState()
  Text("Recent Activities")
  for (a in activities.reversed()) {
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(10.dp)) {
          Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            // Image on the left
            Box(modifier = Modifier.size(100.dp).padding(8.dp)) {
              /*
              Image(
                  bitmap = imageBitmap,
                  contentDescription = "Trail Image",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
              )

               */
            }

            // Spacer between the image and the text
            Spacer(modifier = Modifier.width(8.dp))

            Column {
              Text(text = formatDate(a.timeStarted))
              Text(text = "description")
            }
          }
          // Text information on the right
          Row {
            Column {
              Text(text = String.format("%.2f", metersToKilometers(a.distanceInMeters)))
              Text(text = "Km")
            }

            Column {
              Text(text = a.elevationChangeInMeters.toString())
              Text(text = "Elevation")
            }

            Column {
              Text(text = timeFromMillis(timeFromHikingInMillis(a)))
              Text(text = "Time")
            }
          }
        }
  }
}
