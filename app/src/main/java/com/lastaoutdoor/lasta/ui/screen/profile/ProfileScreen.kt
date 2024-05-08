package com.lastaoutdoor.lasta.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.HikingUserActivity
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.components.DisplaySelection
import com.lastaoutdoor.lasta.ui.components.DropDownMenuComponent
import com.lastaoutdoor.lasta.ui.screen.profile.components.BarGraph
import com.lastaoutdoor.lasta.ui.screen.profile.components.BarType
import com.lastaoutdoor.lasta.utils.DaysInWeek
import com.lastaoutdoor.lasta.utils.MonthsInYear
import com.lastaoutdoor.lasta.utils.TimeFrame
import com.lastaoutdoor.lasta.utils.WeeksInMonth
import com.lastaoutdoor.lasta.utils.Year
import com.lastaoutdoor.lasta.utils.chartDisplayValues
import com.lastaoutdoor.lasta.utils.formatDate
import com.lastaoutdoor.lasta.utils.metersToKilometers
import com.lastaoutdoor.lasta.utils.timeFromActivityInMillis
import com.lastaoutdoor.lasta.utils.timeFromMillis
import java.util.Calendar

@Composable
fun ProfileScreen(
    activities: List<UserActivity>,
    timeFrame: TimeFrame,
    sport: ActivityType,
    isCurrentUser: Boolean,
    user: UserModel,
    updateDescription: (String) -> Unit,
    setSport: (ActivityType) -> Unit,
    setTimeFrame: (TimeFrame) -> Unit,
    navigateToSettings: () -> Unit
) {
  LazyColumn(modifier = Modifier.testTag("ProfileScreen")) {
    item {
      Box(
          modifier =
              Modifier.fillMaxWidth()
                  .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                  .background(MaterialTheme.colorScheme.primary)
                  .padding(16.dp)
                  .height(150.dp)) {
            UserInfo(isCurrentUser, user, updateDescription, navigateToSettings)
          }
    }
    item {
      Box(modifier = Modifier.padding(16.dp)) { SportSelection(sport, setSport) }
      Spacer(modifier = Modifier.height(16.dp))
    }

    item {
      Box(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
          DisplaySelection(
              TimeFrame.values().toList(), timeFrame, setTimeFrame, TimeFrame::toString)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
    item {
      Box(modifier = Modifier.padding(16.dp)) { Chart(activities, timeFrame, sport) }
      Spacer(modifier = Modifier.height(16.dp))
    }
    item { Box(modifier = Modifier.padding(16.dp)) { RecentActivities(activities = activities) } }
  }
}

@Composable
fun UserInfo(
    isCurrentUser: Boolean,
    user: UserModel,
    updateDescription: (String) -> Unit,
    navigateToSettings: () -> Unit
) {
  var isEditBio by rememberSaveable { mutableStateOf(false) }

  ChangeBio(
      isEditBio,
      onDismissRequest = { isEditBio = false },
      bioText = user.description,
      onBioChange = { newBio ->
        updateDescription(newBio)
        isEditBio = false
      })

  Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
      Column {
        AsyncImage(
            model = user.profilePictureUrl,
            contentDescription = "Profile picture",
            modifier =
                Modifier.size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(100.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.default_profile_icon))
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
              Text(
                  text = user.userName,
                  color = MaterialTheme.colorScheme.onPrimary,
                  textAlign = TextAlign.Center,
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold)

              Spacer(modifier = Modifier.width(8.dp))
              if (isCurrentUser) {
                IconButton(
                    onClick = { navigateToSettings() }, modifier = Modifier.testTag("showDialog")) {
                      Icon(
                          Icons.Filled.Menu,
                          contentDescription = "Edit bio",
                          tint = MaterialTheme.colorScheme.onPrimary,
                          modifier = Modifier.size(24.dp))
                    }
              }
            }
        Spacer(modifier = Modifier.height(8.dp))
        // Bio
        Surface(
            modifier = Modifier.fillMaxWidth().height(80.dp),
            shadowElevation = 4.dp,
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primary,
        ) {
          Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                  Text(
                      text = "Bio" /* TODO : make this a xml string */,
                      style = MaterialTheme.typography.bodySmall,
                      modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 8.dp))
                  if (isCurrentUser) {
                    IconButton(
                        onClick = { isEditBio = true },
                        modifier = Modifier.size(24.dp).align(Alignment.Top)) {
                          Icon(
                              Icons.Filled.Create,
                              contentDescription = "Edit bio",
                              tint = MaterialTheme.colorScheme.onPrimary,
                              modifier = Modifier.size(24.dp))
                        }
                  }
                }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                  Text(
                      text = user.description,
                      style = MaterialTheme.typography.bodyMedium,
                      overflow = TextOverflow.Ellipsis,
                      modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp))
                }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeBio(
    isEditBio: Boolean,
    onDismissRequest: () -> Unit,
    bioText: String,
    onBioChange: (String) -> Unit
) {

  if (isEditBio) {
    val maxCharCount = 70
    var text by remember { mutableStateOf(bioText) }

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        modifier = Modifier.fillMaxWidth().wrapContentHeight().testTag("ProfileModal")) {
          Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Bio" /* TODO : make this a xml string */)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = text,
                onValueChange = {
                  if (it.length <= maxCharCount) {
                    text = it
                  }
                },
                placeholder = { Text("Enter your bio...") /* TODO : make this a xml string */ },
                singleLine = false,
                maxLines = 2,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                trailingIcon = {
                  if (text.isNotEmpty()) {
                    Text(
                        "${text.length}/$maxCharCount",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (text.length == maxCharCount) Color.Red else Color.Gray)
                  }
                },
                modifier = Modifier.fillMaxWidth().testTag("ProfileTextField"))

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().testTag("ProfileBox")) {
                  Row {
                    Button(onClick = onDismissRequest) {
                      Text("Cancel" /* TODO : make this a xml string */)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { onBioChange(text) }) {
                      Text("Save" /* TODO : make this a xml string */)
                    }
                  }
                }
          }
        }
  }
}

@Composable
fun SportSelection(sport: ActivityType, onSelected: (ActivityType) -> Unit) {
  Row {
    DropDownMenuComponent(
        items = ActivityType.values().toList(),
        selectedItem = sport,
        onItemSelected = { newSport -> onSelected(newSport) },
        toStr = { s -> s.resourcesToString(LocalContext.current) },
        fieldText = LocalContext.current.getString(R.string.activity))
  }
}

@Composable
fun Chart(activities: List<UserActivity>, timeFrame: TimeFrame, sport: ActivityType) {
  Column(modifier = Modifier.padding(8.dp)) {

    // Bar graph x and y data

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
          ActivityType.HIKING -> {
            Text(
                LocalContext.current.getString(R.string.hikes),
                modifier = Modifier.testTag("TestHike"))
          }
          ActivityType.CLIMBING -> {
            Text(
                LocalContext.current.getString(R.string.climbs),
                modifier = Modifier.testTag("TestClimb"))
          }
          ActivityType.BIKING -> {}
        }
      }
      when (sport) {
        ActivityType.HIKING -> {
          val trailActivities = activities.filterIsInstance<HikingUserActivity>()
          Column {
            Text(
                text = trailActivities.sumOf { it.distanceDone.toLong() }.toString(),
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 20.sp))
            Text(LocalContext.current.getString(R.string.calories))
          }
        }
        ActivityType.CLIMBING -> {
          val trailActivities = activities.filterIsInstance<ClimbingUserActivity>()
          Column {
            Text(
                text = trailActivities.sumOf { it.numPitches }.toString(),
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 20.sp))
            Text(LocalContext.current.getString(R.string.pitches))
          }
        }
        ActivityType.BIKING -> {}
      }

      Column {
        Text(
            text = timeFromMillis(timeFromActivityInMillis(activities)),
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 20.sp))
        Text(LocalContext.current.getString(R.string.time))
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
    activities: List<UserActivity>,
) {
  Text("Recent Activities", style = TextStyle(fontSize = 20.sp), fontWeight = FontWeight.Bold)
  for (a in activities.reversed()) {
    val sport = a.activityType
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
                    ActivityType.HIKING -> {
                      val trail = a as HikingUserActivity
                      Text(
                          text =
                              String.format(
                                  "%.2f", metersToKilometers(trail.distanceDone.toLong())),
                          fontWeight = FontWeight.Bold)
                      Text(text = "Km")
                    }
                    ActivityType.CLIMBING -> {
                      val climb = a as ClimbingUserActivity
                      Text(
                          text =
                              String.format(
                                  "%.2f", metersToKilometers(climb.totalElevation.toLong())),
                          fontWeight = FontWeight.Bold,
                          modifier = Modifier.testTag("TestAndrew1"))
                      Text(text = LocalContext.current.getString(R.string.elevation))
                    }
                    ActivityType.BIKING -> {}
                  }
                }

                Column {
                  when (sport) {
                    ActivityType.HIKING -> {
                      val trail = a as HikingUserActivity
                      Text(text = trail.elevationChange.toString(), fontWeight = FontWeight.Bold)
                      Text(text = LocalContext.current.getString(R.string.elevation))
                    }
                    ActivityType.CLIMBING -> {
                      val climb = a as ClimbingUserActivity
                      Text(
                          text =
                              String.format("%.2f", metersToKilometers(climb.numPitches.toLong())),
                          fontWeight = FontWeight.Bold,
                          modifier = Modifier.testTag("TestAndrew2"))
                      Text(text = LocalContext.current.getString(R.string.pitches))
                    }
                    ActivityType.BIKING -> {}
                  }
                }

                Column {
                  Text(
                      text = timeFromMillis(timeFromActivityInMillis(listOf(a))),
                      fontWeight = FontWeight.Bold)
                  Text(text = LocalContext.current.getString(R.string.time))
                }
              }
        }
  }
}
