package com.lastaoutdoor.lasta.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.lastaoutdoor.lasta.viewmodel.ProfileScreenViewModel
import java.util.Calendar

@Composable
fun ProfileScreen2(
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel(),
) {
  // profileScreenVIewModel.addTrailToUserActivities()
  val trailList by profileScreenViewModel.trails.collectAsState()

  LazyColumn(modifier = Modifier.padding(16.dp)) {
    item {
      SportSelection(profileScreenViewModel)
      Spacer(modifier = Modifier.height(16.dp))
    }

    item {
      TimeFrameSelection(profileScreenViewModel)
      Spacer(modifier = Modifier.height(16.dp))
    }
    item {
      Chart(trailList, profileScreenViewModel)
      Spacer(modifier = Modifier.height(16.dp))
    }
    item {
      RecentActivities(profileScreenViewModel)
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
fun SportSelection(profileScreenViewModel: ProfileScreenViewModel) {
  val sport by profileScreenViewModel.sport.collectAsState()

  Row {

    // Sample data for the Spinner
    val spinnerItems = Sports.values().toList()
    // Observe LiveData and convert to Composable State
    // profileScreenVIewModel.addTrailToUserActivities()
    // Now trailListState is a normal List<Trail> that you can use in Compose

    Spinner(
        items = spinnerItems,
        selectedItem = sport,
        onItemSelected = { newSport -> profileScreenViewModel.setSport(newSport) },
        "Activitiy")
  }
}

@Composable
fun TimeFrameSelection(profileScreenViewModel: ProfileScreenViewModel) {
  val shape = RoundedCornerShape(20.dp)
  val borderModifier =
      Modifier.padding(4.dp).border(width = 1.dp, color = Color.Black, shape = shape)

  Row(
      modifier =
          Modifier.clip(shape)
              .background(MaterialTheme.colorScheme.background)
              .padding(1.dp) // Padding for the border effect
              .then(borderModifier)) {
        val timeFrame by profileScreenViewModel.timeFrame.collectAsState()
        TimeFrame.values().forEach { timeframe ->
          // Determine background and text color based on selection
          val backgroundColor = if (timeFrame == timeframe) Color(0xFFFDB813) else Color.Transparent
          val textColor = if (timeFrame == timeframe) Color.White else Color.Black

          Button(
              onClick = { profileScreenViewModel.setTimeFrame(timeframe) },
              colors =
                  ButtonDefaults.buttonColors(
                      containerColor = backgroundColor, contentColor = textColor),
              shape = shape,
              modifier =
                  Modifier.padding(horizontal = 2.dp)
                      .height(50.dp)
                      .defaultMinSize(minWidth = 50.dp) // Minimum width for all buttons
              ) {
                Text(
                    text = timeframe.name,
                    color = textColor,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp))
              }
        }
      }
}

@Composable
fun Chart(trails: List<Trail>, profileScreenViewModel: ProfileScreenViewModel) {
  Column(modifier = Modifier.padding(8.dp)) {

    // Bar graph x and y data
    val timeFrame by profileScreenViewModel.timeFrame.collectAsState()

    // Based on the collected timeFrame, adapt the chart dynamically
    val (values, abscissa) =
        when (timeFrame) {
          TimeFrame.W -> Pair(chartDisplayValues(trails, timeFrame), DaysInWeek.values().toList())
          TimeFrame.M -> Pair(chartDisplayValues(trails, timeFrame), WeeksInMonth.values().toList())
          TimeFrame.Y -> Pair(chartDisplayValues(trails, timeFrame), MonthsInYear.values().toList())
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
        Text(
            text = String.format("%.2f", values.sum()),
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 48.sp))
        Text("Km")
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Column {
        Text(
            trails.size.toString(),
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 20.sp))
        Text("runs")
      }

      Column {
        Text(
            text = caloriesFromHiking(trails).toString(),
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 20.sp))
        Text("Calories")
      }

      Column {
        Text(
            text = timeFromMillis(timeFromHikingInMillis(*trails.toTypedArray())),
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 20.sp))
        Text("Time")
      }
    }

    Spacer(modifier = Modifier.height(32.dp))

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
fun RecentActivities(profileScreenViewModel: ProfileScreenViewModel) {
  val activities by profileScreenViewModel.allTrails.collectAsState()
  Text("Recent Activities", style = TextStyle(fontSize = 20.sp), fontWeight = FontWeight.Bold)
  for (a in activities.reversed()) {
    Card(
        modifier = Modifier.padding(12.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)) {
          Row(
              modifier = Modifier.padding(8.dp, 0.dp),
              verticalAlignment = Alignment.CenterVertically) {
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

                Column { Text(text = formatDate(a.timeStarted), fontWeight = FontWeight.Bold) }
              }
          // Text information on the right
          Row(
              modifier = Modifier.fillMaxWidth().padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                  Text(
                      text = String.format("%.2f", metersToKilometers(a.distanceInMeters)),
                      fontWeight = FontWeight.Bold)
                  Text(text = "Km")
                }

                Column {
                  Text(text = a.elevationChangeInMeters.toString(), fontWeight = FontWeight.Bold)
                  Text(text = "Elevation")
                }

                Column {
                  Text(
                      text = timeFromMillis(timeFromHikingInMillis(a)),
                      fontWeight = FontWeight.Bold)
                  Text(text = "Time")
                }
              }
        }
  }
}
