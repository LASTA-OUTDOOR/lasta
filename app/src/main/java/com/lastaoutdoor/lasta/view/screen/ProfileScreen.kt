package com.lastaoutdoor.lasta.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.data.model.Sports
import com.lastaoutdoor.lasta.ui.components.BarGraph
import com.lastaoutdoor.lasta.ui.components.BarType
import com.lastaoutdoor.lasta.ui.components.Spinner
import com.lastaoutdoor.lasta.ui.components.TimeFrameSelector
import com.lastaoutdoor.lasta.ui.components.WeekDay
import com.lastaoutdoor.lasta.viewmodel.RecentActivitiesViewModel
import com.lastaoutdoor.lasta.viewmodel.StatisticsViewModel

@Composable
fun ProfileScreen(
    statisticsViewModel: StatisticsViewModel = hiltViewModel(),
    recentActivitiesViewModel: RecentActivitiesViewModel = hiltViewModel()
) {

  Row {
    Text("Activity")

    // Sample data for the Spinner
    val spinnerItems = Sports.values().toList()

    Spinner(
        items = spinnerItems,
        selectedItem = statisticsViewModel.getSport(),
        onItemSelected = { newSport -> statisticsViewModel.setSport(newSport) })
  }

  // Add a vertical space between the author and message texts
  Spacer(modifier = Modifier.height(4.dp))

  TimeFrameSelector()

  // Add a vertical space between the author and message texts
  Spacer(modifier = Modifier.height(4.dp))

  // Bar graph layout
  Column(
      modifier = Modifier.padding(horizontal = 30.dp).fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {

        // Bar graph x and y data
        val ordinate = mutableListOf(5, 10, 15, 0, 5, 7, 11)
        val abscissa = WeekDay.values().toList() // needs to be an enum

        val ordinateFloat: List<Float> = ordinate.map { it.toFloat() / ordinate.max() }

        BarGraph(
            graphBarData = ordinateFloat,
            xAxisScaleData = abscissa,
            barData_ = ordinate,
            height = 300.dp,
            roundType = BarType.TOP_CURVED,
            barWidth = 20.dp,
            barColor = MaterialTheme.colorScheme.primary,
            barArrangement = Arrangement.SpaceEvenly)
      }

  // Add a horizontal space between the image and the column
  Spacer(modifier = Modifier.width(8.dp))

  // Recent activities layout
  LazyVerticalGrid(modifier = Modifier, columns = GridCells.Adaptive(100.dp)) {}
}
