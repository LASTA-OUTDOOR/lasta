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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.data.model.Sports
import com.lastaoutdoor.lasta.data.model.TimeFrame
import com.lastaoutdoor.lasta.data.model.Trail
import com.lastaoutdoor.lasta.ui.components.BarGraph
import com.lastaoutdoor.lasta.ui.components.BarType
import com.lastaoutdoor.lasta.ui.components.Spinner
import com.lastaoutdoor.lasta.ui.components.WeekDay
import com.lastaoutdoor.lasta.utils.weekDisplay
import com.lastaoutdoor.lasta.viewmodel.RecentActivitiesViewModel
import com.lastaoutdoor.lasta.viewmodel.StatisticsViewModel

@Composable
fun ProfileScreen(
    statisticsViewModel: StatisticsViewModel = hiltViewModel(),
    recentActivitiesViewModel: RecentActivitiesViewModel = hiltViewModel()
) {
    //statisticsViewModel.addTrailToUserActivities()
    val trailList by statisticsViewModel.trails.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        SportSelection(statisticsViewModel)
        TimeFrameSelection(statisticsViewModel)
        Chart(trailList)
        RecentActivities(recentActivitiesViewModel)
    }

}

@Composable
fun SportSelection(statisticsViewModel: StatisticsViewModel) {
    Row {
        Text("Activity")

        // Sample data for the Spinner
        val spinnerItems = Sports.values().toList()
        // Observe LiveData and convert to Composable State
        // statisticsViewModel.addTrailToUserActivities()
        // Now trailListState is a normal List<Trail> that you can use in Compose

        Spinner(
            items = spinnerItems,
            selectedItem = statisticsViewModel.getSport(),
            onItemSelected = { newSport -> statisticsViewModel.setSport(newSport) })
    }
}

@Composable
fun TimeFrameSelection(statisticsViewModel: StatisticsViewModel) {
    Row(modifier = Modifier.padding(8.dp)) {
        TimeFrame.values().forEach { timeframe ->
            Button(
                onClick = { statisticsViewModel.setTimeFrame(timeframe) },
                colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                    if (statisticsViewModel.getTimeFrame() == timeframe) Color.Gray else Color.LightGray,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    timeframe.name,
                    color = if (statisticsViewModel.getTimeFrame() == timeframe) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun Chart(trails: List<Trail>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        // Bar graph x and y data
        val values = weekDisplay(trails)

        val ordinate = values.map { it.toInt() }
        val abscissa = WeekDay.values().toList() // needs to be an enum

        val ordinateFloat: List<Float> = values.map {
            val max = ordinate.max()
            if(max == 0) 0f else
            it / ordinate.max()
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
fun RecentActivities(recentActivitiesViewModel: RecentActivitiesViewModel) {
    LazyVerticalGrid(modifier = Modifier, columns = GridCells.Adaptive(100.dp)) {}
}

