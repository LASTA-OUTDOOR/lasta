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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.db.DatabaseManager
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.profile.DaysInWeek
import com.lastaoutdoor.lasta.data.model.profile.MonthsInYear
import com.lastaoutdoor.lasta.data.model.profile.TimeFrame
import com.lastaoutdoor.lasta.data.model.profile.WeeksInMonth
import com.lastaoutdoor.lasta.data.model.profile.Year
import com.lastaoutdoor.lasta.data.model.user.UserLevel
import com.lastaoutdoor.lasta.ui.components.DisplaySelection
import com.lastaoutdoor.lasta.ui.components.DropDownMenuComponent
import com.lastaoutdoor.lasta.ui.navigation.LeafScreen
import com.lastaoutdoor.lasta.ui.screen.profile.components.BarGraph
import com.lastaoutdoor.lasta.ui.screen.profile.components.BarType
import com.lastaoutdoor.lasta.utils.chartDisplayValues
import com.lastaoutdoor.lasta.utils.formatDate
import com.lastaoutdoor.lasta.utils.metersToKilometers
import com.lastaoutdoor.lasta.utils.timeFromActivityInMillis
import com.lastaoutdoor.lasta.utils.timeFromMillis
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel
import com.lastaoutdoor.lasta.viewmodel.ProfileScreenViewModel
import java.util.Calendar

/**
 * Composable function for the ProfileScreen. It displays the user's profile information and
 * activities.
 *
 * @param profileScreenViewModel The ViewModel that holds the state for this screen.
 * @param rootNavController The NavController used for navigation.
 */
@Composable
fun ProfileScreen(
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel(),
    rootNavController: NavHostController,
    navController: NavHostController
) {
  // profileScreenVIewModel.addTrailToUserActivities()
  val activities by profileScreenViewModel.filteredActivities.collectAsState()
  val timeFrame by profileScreenViewModel.timeFrame.collectAsState()
  val sport by profileScreenViewModel.sport.collectAsState()
  val isCurrentUser by profileScreenViewModel.isCurrentUser.collectAsState()

  LazyColumn(modifier = Modifier.testTag("ProfileScreen")) {
    item {
      Box(
          modifier =
              Modifier.fillMaxWidth()
                  .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                  .background(MaterialTheme.colorScheme.primary)
                  .padding(16.dp)
                  .height(150.dp)) {
            UserInfo(rootNavController, navController, isCurrentUser = isCurrentUser)
          }
    }
    item {
      Box(modifier = Modifier.padding(16.dp)) {
        SportSelection(sport, profileScreenViewModel::setSport)
      }
      Spacer(modifier = Modifier.height(16.dp))
    }

    item {
      Box(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
          DisplaySelection(
              TimeFrame.values().toList(),
              timeFrame,
              profileScreenViewModel::setTimeFrame,
              TimeFrame::toString)
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

/**
 * Composable function for displaying user information. It displays the user's profile picture,
 * name, and bio.
 *
 * @param rootNavController The NavController used for navigation.
 * @param authViewModel The ViewModel that holds the authentication state.
 * @param preferencesViewModel The ViewModel that holds the user preferences.
 * @param profileScreenViewModel The ViewModel that holds the state for the ProfileScreen.
 * @param isCurrentUser A boolean indicating if the displayed user is the current user.
 */
@Composable
fun UserInfo(
    rootNavController: NavHostController,
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel(),
    isCurrentUser: Boolean
) {
  val user = profileScreenViewModel.user.collectAsState()
  val userName = user.value.userName
  val profilePictureUrl = user.value.profilePictureUrl
  val hikingLevel = user.value.userLevel
  val bio = user.value.bio
  var isEditBio by rememberSaveable { mutableStateOf(false) }

  ChangeBio(
      isEditBio,
      onDismissRequest = { isEditBio = false },
      bioText = bio ?: "",
      onBioChange = { newBio ->
        preferencesViewModel.updateBio(newBio)
        DatabaseManager().updateFieldInUser(user.value.userId, "bio", newBio)
        isEditBio = false
      })

  Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
      Column {
        AsyncImage(
            model = profilePictureUrl,
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
                  text = userName ?: "",
                  color = MaterialTheme.colorScheme.onPrimary,
                  textAlign = TextAlign.Center,
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold)

              Spacer(modifier = Modifier.width(8.dp))
              if (isCurrentUser) {
                IconButton(
                    onClick = { navController.navigate(LeafScreen.Settings.route) },
                    modifier = Modifier.testTag("showDialog")) {
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
                      text = bio ?: "",
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
        modifier = Modifier.fillMaxWidth().wrapContentHeight().testTag("")) {
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
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
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
fun HikingRow(
    selectedUserLevel: UserLevel,
    preferences: PreferencesViewModel = hiltViewModel(),
) {
  val userId by preferences.userId.collectAsState(initial = "")
  Row(
      modifier = Modifier.fillMaxWidth(.7f),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically) {
        UserLevel.values().forEachIndexed { index, hikingLevel ->
          Text(text = hikingLevel.toString(), maxLines = 1, overflow = TextOverflow.Ellipsis)
          RadioButton(
              modifier = Modifier.testTag("HikingLevelItem$index"),
              selected = hikingLevel == selectedUserLevel,
              onClick = {
                preferences.updateHikingLevel(hikingLevel)
                DatabaseManager().updateFieldInUser(userId, "hikingLevel", hikingLevel.toString())
              })
        }
      }
}

@Composable
fun SportSelection(
    sport: ActivitiesDatabaseType.Sports,
    onSelected: (ActivitiesDatabaseType.Sports) -> Unit
) {
  Row {
    // Sample data for the Spinner
    val menuItems = ActivitiesDatabaseType.Sports.values().toList()
    // Observe LiveData and convert to Composable State
    // profileScreenVIewModel.addTrailToUserActivities()
    // Now trailListState is a normal List<Trail> that you can use in Compose
    val con = LocalContext.current
    DropDownMenuComponent<ActivitiesDatabaseType.Sports>(
        items = menuItems,
        selectedItem = sport,
        onItemSelected = { newSport -> onSelected(newSport) },
        toStr = { it.toStringCon(con) },
        LocalContext.current.getString(R.string.activity))
  }
}

@Composable
fun Chart(
    activities: List<ActivitiesDatabaseType>,
    timeFrame: TimeFrame,
    sport: ActivitiesDatabaseType.Sports
) {
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
          ActivitiesDatabaseType.Sports.HIKING -> {
            Text(LocalContext.current.getString(R.string.hikes))
          }
          ActivitiesDatabaseType.Sports.CLIMBING -> {
            Text(
                LocalContext.current.getString(R.string.climbs),
                modifier = Modifier.testTag("TestClimb"))
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
            Text(LocalContext.current.getString(R.string.calories))
          }
        }
        ActivitiesDatabaseType.Sports.CLIMBING -> {
          val trailActivities = activities.filterIsInstance<ActivitiesDatabaseType.Climb>()
          Column {
            Text(
                text = trailActivities.sumOf { it.numberOfPitches }.toString(),
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 20.sp))
            Text(LocalContext.current.getString(R.string.pitches))
          }
        }
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
                      Text(text = LocalContext.current.getString(R.string.elevation))
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
                      Text(text = LocalContext.current.getString(R.string.elevation))
                    }
                    ActivitiesDatabaseType.Sports.CLIMBING -> {
                      val climb = a as ActivitiesDatabaseType.Climb
                      Text(
                          text = String.format("%.2f", metersToKilometers(climb.numberOfPitches)),
                          fontWeight = FontWeight.Bold,
                          modifier = Modifier.testTag("TestAndrew2"))
                      Text(text = LocalContext.current.getString(R.string.pitches))
                    }
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
