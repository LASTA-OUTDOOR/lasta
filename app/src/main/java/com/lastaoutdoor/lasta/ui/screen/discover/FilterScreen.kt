package com.lastaoutdoor.lasta.ui.screen.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.ui.components.DropDownMenuComponent
import com.lastaoutdoor.lasta.ui.screen.discover.components.ToggleButton
import com.lastaoutdoor.lasta.ui.theme.AccentGreen
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterScreen(
    discoverScreenState: StateFlow<DiscoverScreenState>,
    navigateBack: () -> Unit
) {

  val userSelectedLevels = discoverScreenState.collectAsState().value.selectedLevels
  val selectedActivitiesType = discoverScreenState.collectAsState().value.selectedActivityTypes

  val activities = ActivityType.values()


  val activitiesLevelArray = remember {
    mutableStateListOf(
        userSelectedLevels.climbingLevel,
        userSelectedLevels.hikingLevel,
        userSelectedLevels.bikingLevel)
  }

  val selectedActivitiesTypes = remember {
    mutableStateListOf(selectedActivitiesType.first())
  }

  var checkedBox by remember { mutableStateOf(true) }

  Column(modifier = Modifier
      .fillMaxSize()
      .testTag("filterScreen")) {
    MediumTopAppBar(
        title = {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(id = R.string.filter_options),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground)
          }
        },
        navigationIcon = {
          IconButton(onClick = { navigateBack() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
          }
        })

    HorizontalDivider(modifier = Modifier.fillMaxWidth())

    Spacer(modifier = Modifier.fillMaxHeight(0.025f))

    // Filter by activity type
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), horizontalAlignment = Alignment.Start) {
          Column {
            Text(
                text = stringResource(id = R.string.filter_activity_type),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)

            FlowRow(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .fillMaxWidth()) {
                  activities.forEach { activity ->
                    ToggleButton(
                        activity.resourcesToString(LocalContext.current),
                        isSelected = selectedActivitiesTypes.contains(activity)) {
                          if (!selectedActivitiesTypes.contains(activity)) {
                            selectedActivitiesTypes.add(activity)
                          } else {
                            selectedActivitiesTypes.remove(activity)
                          }
                        }
                  }
                }
          }

          Spacer(modifier = Modifier.fillMaxHeight(0.05f))
          // Filter by difficulty level
          Column {
            Text(
                text = stringResource(id = R.string.filter_difficulty_level),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)

            Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
              ActivityType.values().forEachIndexed { index, activityType ->
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                  val isEnabled = selectedActivitiesTypes.contains(activityType)
                  Button(
                      onClick = {},
                      modifier = Modifier.testTag("difficultyLevelButton$index"),
                      enabled = isEnabled,
                      shape = MaterialTheme.shapes.small,
                      colors =
                          ButtonDefaults.buttonColors(
                              containerColor = MaterialTheme.colorScheme.surface,
                              contentColor = MaterialTheme.colorScheme.onBackground,
                              disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                              disabledContentColor =
                                  MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                          ),
                      elevation = ButtonDefaults.elevatedButtonElevation(1.dp)) {
                        DropDownMenuComponent(
                            items = UserLevel.values().toList(),
                            selectedItem = activitiesLevelArray[index],
                            onItemSelected = { activitiesLevelArray[index] = it },
                            toStr = { level -> level.resourcesToString(LocalContext.current) },
                            fieldText = activityType.resourcesToString(LocalContext.current),
                            isEnabled = isEnabled)
                      }
                }
              }
            }
          }

          Spacer(modifier = Modifier.fillMaxHeight(0.05f))

          // Enable the user to see or not his/her completed activities
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Checkbox(
                  checked = checkedBox,
                  onCheckedChange = { checkedBox = it },
                  colors =
                      CheckboxDefaults.colors(
                          uncheckedColor = MaterialTheme.colorScheme.onBackground,
                          checkedColor = AccentGreen))
              Text(
                  text = stringResource(id = R.string.show_completed_activities),
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.Bold,
              )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)) {
                  Text(
                      text = stringResource(id = R.string.show_completed_activities_description),
                      style = MaterialTheme.typography.bodyMedium,
                      color = Color.Gray,
                  )
                }
          }
          Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)) {
              Button(
                  onClick = { /* TODO */},
                  modifier = Modifier.testTag("EraseButton"),
                  colors =
                      ButtonDefaults.buttonColors(
                          containerColor = MaterialTheme.colorScheme.background,
                          contentColor =
                              MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))) {
                    Text(
                        stringResource(id = R.string.erase_options),
                        style = MaterialTheme.typography.headlineSmall)
                  }
              // Apply the filter options
              ElevatedButton(
                  onClick = {
                    { /* TODO */}
                    navigateBack()
                  },
                  elevation = ButtonDefaults.elevatedButtonElevation(3.dp),
                  modifier = Modifier
                      .testTag("applyFilterOptionsButton")
                      .weight(0.6f),
                  colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                    Text(
                        LocalContext.current.getString(R.string.apply),
                        style = MaterialTheme.typography.headlineMedium)
                  }
            }
          }
        }
  }
}
