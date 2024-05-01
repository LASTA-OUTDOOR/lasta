@file:Suppress("NAME_SHADOWING")

package com.lastaoutdoor.lasta.ui.screen.discover

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.ui.theme.AccentGreen
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    selectedLevels: StateFlow<UserActivitiesLevel>,
    setSelectedLevels: (UserActivitiesLevel) -> Unit,
    selectedActivityType: StateFlow<ActivityType>,
    setSelectedActivityType: (ActivityType) -> Unit,
    navigateBack: () -> Unit
) {
  var fromDistance by remember { mutableStateOf(0) }
  var toDistance by remember { mutableStateOf(1000) }

  var activities = ActivityType.values()
  var initialSelectedActivityType = selectedActivityType.collectAsState().value
  var selectedActivity by remember { mutableStateOf(initialSelectedActivityType) }
  var selectedIndex by remember { mutableIntStateOf(activities.indexOf(selectedActivity)) }

  var userActivitiesLevel = UserLevel.values()
  var initialSelectedLevels = selectedLevels.collectAsState().value
  var selectedLevels by remember { mutableStateOf(initialSelectedLevels) }
  var selectedActivityLevel by remember { mutableStateOf(activities.indexOf(selectedLevels)) }
  //Update selected levels when the selected activity changes
  SideEffect {
      val indexSelected = activities.indexOf(selectedActivityType.value)
      selectedActivityLevel = when (indexSelected) {
          0 -> selectedLevels.climbingLevel
          1 -> selectedLevels.hikingLevel
          2 -> selectedLevels.bikingLevel
          else -> UserLevel.BEGINNER
      }
  }

  Column(
      modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
          .testTag("filterScreen"),
      horizontalAlignment = Alignment.CenterHorizontally) {

        // return button to go back to the discovery screen
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.filter_options)) },
            navigationIcon = {
              IconButton(onClick = { navigateBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
              }
            })

        Spacer(modifier = Modifier.height(16.dp))

        // Filter by activity type
        Text(
            text = stringResource(id = R.string.filter_activity_type),
            style = TextStyle(fontSize = 20.sp, lineHeight = 24.sp, fontWeight = FontWeight(500)))
        Row(verticalAlignment = Alignment.CenterVertically) {
          activities.forEachIndexed { index, activityType ->
            RadioButton(selected = index == selectedIndex, onClick = { selectedIndex = index })
            Text(text = activityType.toString())
          }
        }

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(thickness = 2.dp, color = PrimaryBlue)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.filter_difficulty_level),
            style = TextStyle(fontSize = 20.sp, lineHeight = 24.sp, fontWeight = FontWeight(500)))
        Row(verticalAlignment = Alignment.CenterVertically) {
          userActivitiesLevel.forEachIndexed { index, difficultyLevel ->
            RadioButton(
                selected = index == indexSelected, // TODO: link with database
                onClick = {
                    indexSelected = index
                    setSelectedLevels(
                        when (selectedActivity) {
                            ActivityType.CLIMBING -> selectedLevels.copy(climbingLevel = difficultyLevel)
                            ActivityType.HIKING -> selectedLevels.copy(hikingLevel = difficultyLevel)
                            ActivityType.BIKING -> selectedLevels.copy(bikingLevel = difficultyLevel)
                        })
                })
            Text(text = difficultyLevel.toString())
          }
        }

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(thickness = 2.dp, color = PrimaryBlue)
        Spacer(modifier = Modifier.height(4.dp))

        // Enable the user to see or not his/her completed activities
        Row(verticalAlignment = Alignment.CenterVertically) {
          Checkbox(
              checked = false, // TODO: Bind this to your actual data
              onCheckedChange = { /*TODO*/},
              colors =
                  CheckboxDefaults.colors(uncheckedColor = AccentGreen, checkedColor = AccentGreen))
          Text(
              text = stringResource(id = R.string.show_completed_activities),
              style =
                  TextStyle(
                      fontSize = 20.sp,
                      lineHeight = 24.sp,
                      fontWeight = FontWeight(500),
                      color = AccentGreen))
        }

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(thickness = 2.dp, color = PrimaryBlue)
        Spacer(modifier = Modifier.height(12.dp))

        // Filter by distance range
        Text(
            text = stringResource(id = R.string.filter_distance_range),
            style = TextStyle(fontSize = 20.sp, lineHeight = 24.sp, fontWeight = FontWeight(500)))
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = stringResource(id = R.string.from))
          Spacer(modifier = Modifier.width(12.dp))
          // input field for the user to input the distance range lo
          TextField(
              value = fromDistance.toString(),
              onValueChange = { fromDistance = it.toIntOrNull() ?: 0 },
              modifier = Modifier.width(50.dp))
          Spacer(modifier = Modifier.width(12.dp))
          Text(text = stringResource(id = R.string.to))
          Spacer(modifier = Modifier.width(12.dp))
          // input field for the user to input the distance range hi
          TextField(
              value = toDistance.toString(),
              onValueChange = { toDistance = it.toIntOrNull() ?: 0 },
              modifier = Modifier.width(100.dp))
          Spacer(modifier = Modifier.width(12.dp))
          Text(text = "km")
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 2.dp, color = PrimaryBlue)
        Spacer(modifier = Modifier.height(12.dp))
        // Apply the filter options
        ElevatedButton(
            onClick = {
              setSelectedActivityType(activities[selectedIndex])
              navigateBack()
            },
            modifier = Modifier
                .width(305.dp)
                .height(48.dp)
                .testTag("applyFilterOptionsButton"),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
              Text(
                  LocalContext.current.getString(R.string.apply),
                  style =
                      TextStyle(
                          fontSize = 22.sp,
                          lineHeight = 28.sp,
                          fontWeight = FontWeight(400),
                      ))
            }
      }
}
