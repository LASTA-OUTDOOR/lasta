package com.lastaoutdoor.lasta.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.ActivityType
import com.lastaoutdoor.lasta.data.model.OutdoorActivity
import com.lastaoutdoor.lasta.viewmodel.OutdoorActivityViewModel

@Composable
fun DiscoveryScreen(outdoorActivityViewModel: OutdoorActivityViewModel = viewModel()) {
  /** this is called when discovery button is clicked */
  Scaffold(floatingActionButton = { FloatingActionButtons(outdoorActivityViewModel) }) {
      innerPadding ->
    Column(modifier = Modifier.padding(innerPadding)) { DiscoveryContent(outdoorActivityViewModel) }
  }
}

@Composable
fun DiscoveryContent(outdoorActivityViewModel: OutdoorActivityViewModel) {
  Column {
    // link this with database or API depending from which we fetch
    // OutdoorActivityList(outdoorActivityViewModel)

    // example
    var outdoorList =
        List(10) { index -> OutdoorActivity(ActivityType.HIKING, 3, 5.0f, "2 hours", "Zurich") }

    OutdoorActivityList(outdoorList,outdoorActivityViewModel)
  }
}

@Composable
fun FloatingActionButtons(outdoorActivityViewModel: OutdoorActivityViewModel) {
  Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.Bottom,
      horizontalAlignment = Alignment.End) {
        FloatingActionButton(
            onClick = { outdoorActivityViewModel.refresh() },
            modifier = Modifier.padding(bottom = 8.dp)) {
              Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
            }
        FloatingActionButton(onClick = { outdoorActivityViewModel.filter() }) {
          Icon(Icons.Filled.Build, contentDescription = "Filter")
        }
      }
}

@Composable
fun OutdoorActivityList(outdoorActivities: List<OutdoorActivity>,outdoorActivityViewModel: OutdoorActivityViewModel) {
  /** Our list of activities which is lazy in order to display only the first ones. */
  LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
    items(outdoorActivities) { outdoorActivity -> OutdoorActivityItem(outdoorActivity,outdoorActivityViewModel) }
  }
}

@Composable
fun OutdoorActivityItem(outdoorActivity: OutdoorActivity,outdoorActivityViewModel: OutdoorActivityViewModel) {
  Card(modifier = Modifier.padding(8.dp)) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
      if (outdoorActivityViewModel.getDialogState()) {
        ActivityDialog(
            onDismissRequest = { outdoorActivityViewModel.setDialogState(false) },
            outdoorActivity = outdoorActivity)
      }
      Row(
          // modifier = Modifier.fillMaxWidth(),
          ) {
            Text(
                text = outdoorActivity.getActivityType().toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp)
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
              Text(text = outdoorActivity.locationName)
              Text(text = "Difficulty : ${outdoorActivity.difficulty}/10")
            }
          }
      Spacer(modifier = Modifier.height(25.dp))

      Button(
          onClick = { /* Switch page and start activity itinerary */},
          // set its theme from themes.xml

          modifier =
              Modifier.border(
                      width = 1.dp,
                      color = Color(0xFFFF7009),
                      shape = RoundedCornerShape(size = 20.dp))
                  // .width(104.dp)
                  // .height(30.dp)
                  .background(
                      color = Color(0xFFFF7009), shape = RoundedCornerShape(size = 20.dp))) {
            Text(
                text = "START",
                style =
                    TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    ))
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                modifier =
                    Modifier.shadow(
                            elevation = 4.dp,
                            spotColor = Color(0x40000000),
                            ambientColor = Color(0x40000000))
                        .padding(1.dp)
                        .width(16.dp)
                        .height(19.dp),
                // will need to change resource to play_button
                painter = painterResource(id = R.drawable.play_button),
                contentDescription = "play image for button",
                contentScale = ContentScale.Crop)
          }
      Row() {
        Button(
            onClick = { outdoorActivityViewModel.setDialogState(true) },
            modifier =
                Modifier.border(
                        width = 1.dp,
                        color = Color(0xFF6609FF),
                        shape = RoundedCornerShape(size = 20.dp))
                    // .width(104.dp)
                    // .height(30.dp)
                    .background(
                        color = Color(0xFF6609FF), shape = RoundedCornerShape(size = 20.dp))) {
              Text(
                  text = "MORE INFO",
                  style =
                      TextStyle(
                          fontSize = 15.sp,
                          fontWeight = FontWeight(700),
                          color = Color(0xFFFFFFFF),
                          textAlign = TextAlign.Center,
                      ))
            }

        Button(
            onClick = { /* Switch to map view and see location of activity */},
            modifier =
                Modifier.border(
                        width = 1.dp,
                        color = Color(0xFF0989FF),
                        shape = RoundedCornerShape(size = 20.dp))
                    // .width(104.dp)
                    // .height(30.dp)
                    .background(
                        color = Color(0xFF0989FF), shape = RoundedCornerShape(size = 20.dp))) {
              Text(
                  text = "VIEW ON MAP",
                  style =
                      TextStyle(
                          fontSize = 15.sp,
                          fontWeight = FontWeight(700),
                          color = Color(0xFFFFFFFF),
                          textAlign = TextAlign.Center,
                      ))
            }
      }
    }
  }
}

@Composable
fun OutdoorActivityExample(outdoorActivityViewModel: OutdoorActivityViewModel) {
  MaterialTheme {
    OutdoorActivityItem(OutdoorActivity(ActivityType.HIKING, 3, 5.0f, "2 hours", "Zurich"),outdoorActivityViewModel)
  }
}

@Composable
fun OutdoorActivityListExample(outdoorActivityViewModel: OutdoorActivityViewModel) {
  MaterialTheme {
    OutdoorActivityList(
        List(10) { index -> OutdoorActivity(ActivityType.HIKING, 3, 5.0f, "2 hours", "Zurich") },
        outdoorActivityViewModel)
  }
}

@Composable
fun ActivityDialog(
    onDismissRequest: () -> Unit,
    outdoorActivity: OutdoorActivity
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = "Location: " + outdoorActivity.locationName,
                    modifier = Modifier.padding(16.dp),
                )
                Text(
                    text = "Duration: " + outdoorActivity.duration,
                    modifier = Modifier.padding(16.dp),
                )
                Text(
                    text = "Difficulty: ${outdoorActivity.difficulty}",
                    modifier = Modifier.padding(16.dp),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}