package com.lastaoutdoor.lasta.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lastaoutdoor.lasta.model.data.OutdoorActivity
import com.lastaoutdoor.lasta.viewmodel.OutdoorActivityViewModel
import java.util.Vector

@Composable
fun OutdoorActivityScreen(outdoorActivityViewModel: OutdoorActivityViewModel = viewModel()) {
    /** this is called when discovery button is clicked */
    Scaffold(
        floatingActionButton = {
            FloatingActionButtons(outdoorActivityViewModel)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            DiscoveryContent(outdoorActivityViewModel)
        }
    }
}

@Composable
fun DiscoveryContent(outdoorActivityViewModel: ViewModel) {
    Column {
        //link this with database or API depending from which we fetch
        //OutdoorActivityList(outdoorActivityViewModel)

        //example
        var outdoorList =
            List(10) { index ->
                OutdoorActivity(
                    "Hiking", 3, 5.0f, "2 hours", "Zurich",
                    Vector(listOf(47.0f, 8.0f))
                )
            }
        OutdoorActivityList(outdoorList)
    }
}

@Composable
fun FloatingActionButtons(outdoorActivityViewModel: OutdoorActivityViewModel) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = { outdoorActivityViewModel.refresh() },
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
        }
        FloatingActionButton(
            onClick = { outdoorActivityViewModel.filter() }
        ) {
            Icon(Icons.Filled.Build, contentDescription = "Filter")
        }
    }
}

@Composable
fun OutdoorActivityList(outdoorActivities: List<OutdoorActivity>) {
    /** Lazy Column only composes and lays out the items
    that are visible on the screen, which makes it very efficient
    for long lists. This also delays downloading images. */

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(outdoorActivities) { outdoorActivity ->
            OutdoorActivityItem(outdoorActivity)
        }
    }
}

@Composable
fun OutdoorActivityItem(outdoorActivity: OutdoorActivity) {
    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = outdoorActivity.locationName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Estimated Difficulty : ${outdoorActivity.difficulty}"
                )
            }
        }
    }
}


