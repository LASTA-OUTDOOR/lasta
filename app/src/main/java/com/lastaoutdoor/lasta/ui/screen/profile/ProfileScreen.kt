package com.lastaoutdoor.lasta.ui.screen.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.profile.DaysInWeek
import com.lastaoutdoor.lasta.data.model.profile.MonthsInYear
import com.lastaoutdoor.lasta.data.model.profile.TimeFrame
import com.lastaoutdoor.lasta.data.model.profile.WeeksInMonth
import com.lastaoutdoor.lasta.data.model.profile.Year
import com.lastaoutdoor.lasta.data.model.user.HikingLevel
import com.lastaoutdoor.lasta.ui.navigation.RootScreen
import com.lastaoutdoor.lasta.ui.screen.profile.components.BarGraph
import com.lastaoutdoor.lasta.ui.screen.profile.components.BarType
import com.lastaoutdoor.lasta.ui.screen.profile.components.Spinner
import com.lastaoutdoor.lasta.utils.chartDisplayValues
import com.lastaoutdoor.lasta.utils.formatDate
import com.lastaoutdoor.lasta.utils.metersToKilometers
import com.lastaoutdoor.lasta.utils.timeFromActivityInMillis
import com.lastaoutdoor.lasta.utils.timeFromMillis
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel
import com.lastaoutdoor.lasta.viewmodel.ProfileScreenViewModel
import java.util.Calendar

@Composable
fun ProfileScreen(
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel(),
    rootNavController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
  // profileScreenVIewModel.addTrailToUserActivities()
  val activities by profileScreenViewModel.trails.collectAsState()

  LazyColumn(modifier = Modifier.padding(16.dp).testTag("ProfileScreen")) {
    item { UserInfo(rootNavController) }
    item {
      SportSelection()
      Spacer(modifier = Modifier.height(16.dp))
    }

    item {
      TimeFrameSelection()
      Spacer(modifier = Modifier.height(16.dp))
    }
    item {
      Chart(activities)
      Spacer(modifier = Modifier.height(16.dp))
    }
    item { RecentActivities(activities = activities) }
  }
}

@Composable
fun UserInfo(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
  // val isLoggedIn by preferencesViewModel.isLoggedIn.collectAsState(initial = false)
  // val userId by preferencesViewModel.userId.collectAsState(initial = "")
  val userName by preferencesViewModel.userName.collectAsState(initial = "")
  // val email by preferencesViewModel.email.collectAsState(initial = "")
  val profilePictureUrl by preferencesViewModel.profilePictureUrl.collectAsState(initial = "")
  val hikingLevel by preferencesViewModel.hikingLevel.collectAsState(initial = HikingLevel.BEGINNER)

  LaunchedEffect(key1 = authViewModel.signedOut) {
    if (authViewModel.signedOut) {
      preferencesViewModel.clearPreferences()
      preferencesViewModel.updateIsLoggedIn(false)
      rootNavController.popBackStack()
      rootNavController.navigate(RootScreen.Login.route)
    }
  }

  var showDialog by remember { mutableStateOf(false) }

  if (showDialog) {
    AlertDialog(
        modifier = Modifier.testTag("AlertDialog"),
        onDismissRequest = { showDialog = false },
        title = { Text("Preferences") },
        text = {
          Column {
            Text("Hiking Level")
            HikingRow(selectedHikingLevel = hikingLevel)
          }
        },
        confirmButton = { Row { Button(onClick = { showDialog = false }) { Text("Save") } } },
        dismissButton = {
          Button(
              onClick = {
                showDialog = false
                authViewModel.signOut()
                preferencesViewModel.clearPreferences()
                preferencesViewModel.updateIsLoggedIn(false)
                rootNavController.popBackStack()
                rootNavController.navigate(RootScreen.Login.route)
              }) {
                Text("Sign out")
              }
        })
  }

  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
    Column {
      Button(onClick = { showDialog = true }, modifier = Modifier.testTag("showDialog")) {
        Text("≡")
      }
    }
  }
  Spacer(modifier = Modifier.height(8.dp))
  Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Column {
      AsyncImage(
          model = profilePictureUrl,
          contentDescription = "Profile picture",
          modifier = Modifier.size(70.dp).clip(CircleShape),
          contentScale = ContentScale.Crop)
    }
    Column(modifier = Modifier.padding(0.dp, 8.dp, 16.dp, 0.dp)) {
      Text(
          text = userName,
          color = MaterialTheme.colorScheme.primary,
          textAlign = TextAlign.Center,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold)
    }
  }
  Spacer(modifier = Modifier.height(16.dp))
  HorizontalDivider(
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
      thickness = 1.dp, // Specify the thickness of the divider
  )
  Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun HikingRow(
    selectedHikingLevel: HikingLevel,
    preferences: PreferencesViewModel = hiltViewModel(),
) {
  Row(
      modifier = Modifier.fillMaxWidth(.7f),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically) {
        HikingLevel.values().forEachIndexed { index, hikingLevel ->
          Text(text = hikingLevel.level.toString(), maxLines = 1, overflow = TextOverflow.Ellipsis)
          RadioButton(
              modifier = Modifier.testTag("HikingLevelItem$index"),
              selected = hikingLevel == selectedHikingLevel,
              onClick = { preferences.updateHikingLevel(hikingLevel) })
        }
      }
}

@Composable
fun SportSelection(profileScreenViewModel: ProfileScreenViewModel = hiltViewModel()) {
  val sport by profileScreenViewModel.sport.collectAsState()

  Row {
    // Sample data for the Spinner
    val spinnerItems = ActivitiesDatabaseType.Sports.values().toList()
    // Observe LiveData and convert to Composable State
    // profileScreenVIewModel.addTrailToUserActivities()
    // Now trailListState is a normal List<Trail> that you can use in Compose

    Spinner(
        items = spinnerItems,
        selectedItem = sport,
        onItemSelected = { newSport -> profileScreenViewModel.setSport(newSport) },
        "Activity")
  }
}

@Composable
fun TimeFrameSelection(profileScreenViewModel: ProfileScreenViewModel = hiltViewModel()) {
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
        TimeFrame.values().forEachIndexed { index, timeframe ->
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
                  Modifier.testTag("TimeFrameItem$index")
                      .padding(horizontal = 2.dp)
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
fun Chart(
    activities: List<ActivitiesDatabaseType>,
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel()
) {
  Column(modifier = Modifier.padding(8.dp)) {

    // Bar graph x and y data
    val timeFrame by profileScreenViewModel.timeFrame.collectAsState()
    val sport by profileScreenViewModel.sport.collectAsState()

    // Based on the collected timeFrame, adapt the chart dynamically
    val (values, abscissa) =
        when (timeFrame) {
          TimeFrame.W ->
              Pair(chartDisplayValues(activities, timeFrame), DaysInWeek.values().toList())
          TimeFrame.M ->
              Pair(chartDisplayValues(activities, timeFrame), WeeksInMonth.values().toList())
          TimeFrame.Y ->
              Pair(chartDisplayValues(activities, timeFrame), MonthsInYear.values().toList())
          TimeFrame.ALL -> {
            if (activities.isEmpty()) {
              Pair(listOf(0f), listOf(Year(2024)))
            } else {
              val start =
                  Calendar.getInstance()
                      .apply { time = activities[0].timeStarted }
                      .get(Calendar.YEAR)
              val end =
                  Calendar.getInstance()
                      .apply { time = activities[activities.size - 1].timeStarted }
                      .get(Calendar.YEAR)
              val years = mutableListOf<Year>()
              for (i in start..end) {
                years.add(Year(i))
              }
              Pair(chartDisplayValues(activities, timeFrame), years.toList())
            }
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
            activities.size.toString(),
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 20.sp))

        when (sport) {
          ActivitiesDatabaseType.Sports.HIKING -> {
            Text("hikes")
          }
          ActivitiesDatabaseType.Sports.CLIMBING -> {
            Text("climbs", modifier = Modifier.testTag("TestClimb"))
          }
        }
      }
      when (sport) {
        ActivitiesDatabaseType.Sports.HIKING -> {
          val trailActivities = activities.filterIsInstance<ActivitiesDatabaseType.Trail>()
          Column {
            Text(
                text = trailActivities.sumOf { it.caloriesBurned }.toString(),
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 20.sp))
            Text("Calories")
          }
        }
        ActivitiesDatabaseType.Sports.CLIMBING -> {
          val trailActivities = activities.filterIsInstance<ActivitiesDatabaseType.Climb>()
          Column {
            Text(
                text = trailActivities.sumOf { it.numberOfPitches }.toString(),
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 20.sp))
            Text("Pitches")
          }
        }
      }

      Column {
        Text(
            text = timeFromMillis(timeFromActivityInMillis(activities)),
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
        height = 260.dp,
        roundType = BarType.TOP_CURVED,
        barWidth = 20.dp,
        barColor = MaterialTheme.colorScheme.primary,
        barArrangement = Arrangement.SpaceEvenly)
  }
}

@Composable
fun RecentActivities(
    activities: List<ActivitiesDatabaseType>,
) {
  Text("Recent Activities", style = TextStyle(fontSize = 20.sp), fontWeight = FontWeight.Bold)
  for (a in activities.reversed()) {
    val sport = a.sport
    Card(
        modifier = Modifier.padding(12.dp).fillMaxWidth().testTag("RecentActivitiesItem"),
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
                  when (sport) {
                    ActivitiesDatabaseType.Sports.HIKING -> {
                      val trail = a as ActivitiesDatabaseType.Trail
                      Text(
                          text = String.format("%.2f", metersToKilometers(trail.distanceInMeters)),
                          fontWeight = FontWeight.Bold)
                      Text(text = "Km")
                    }
                    ActivitiesDatabaseType.Sports.CLIMBING -> {
                      val climb = a as ActivitiesDatabaseType.Climb
                      Text(
                          text =
                              String.format(
                                  "%.2f", metersToKilometers(climb.elevationGainedInMeters)),
                          fontWeight = FontWeight.Bold,
                          modifier = Modifier.testTag("TestAndrew1"))
                      Text(text = "Elevation")
                    }
                  }
                }

                Column {
                  when (sport) {
                    ActivitiesDatabaseType.Sports.HIKING -> {
                      val trail = a as ActivitiesDatabaseType.Trail
                      Text(
                          text = trail.elevationChangeInMeters.toString(),
                          fontWeight = FontWeight.Bold)
                      Text(text = "Elevation")
                    }
                    ActivitiesDatabaseType.Sports.CLIMBING -> {
                      val climb = a as ActivitiesDatabaseType.Climb
                      Text(
                          text = String.format("%.2f", metersToKilometers(climb.numberOfPitches)),
                          fontWeight = FontWeight.Bold,
                          modifier = Modifier.testTag("TestAndrew2"))
                      Text(text = "Pitches")
                    }
                  }
                }

                Column {
                  Text(
                      text = timeFromMillis(timeFromActivityInMillis(listOf(a))),
                      fontWeight = FontWeight.Bold)
                  Text(text = "Time")
                }
              }
        }
  }
}
