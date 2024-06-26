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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenCallBacks
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterScreen(
    discoverScreenState: StateFlow<DiscoverScreenState>,
    discoverScreenCallBacks: DiscoverScreenCallBacks,
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
  val checked = discoverScreenState.collectAsState().value.showCompleted
  // internal function to avoid code duplication
  @Composable
  fun collectSelectedActivitiesTypes(): SnapshotStateList<ActivityType> {
    // really cumbersome way to get the number of activities but didn't want to break the rest of
    // the
    // code (implemented by someone else)
    val nrOfActivities = selectedActivitiesType.size
    return when (nrOfActivities) {
      0 -> remember { mutableStateListOf() }
      1 -> remember { mutableStateListOf(selectedActivitiesType.first()) }
      2 ->
          remember {
            mutableStateListOf(selectedActivitiesType.first(), selectedActivitiesType.last())
          }
      else ->
          remember {
            mutableStateListOf(
                selectedActivitiesType.first(),
                selectedActivitiesType[1],
                selectedActivitiesType.last())
          }
    }
  }

  val snapshotSelectedActivitiesTypes = collectSelectedActivitiesTypes()

  val selectedActivitiesTypes = collectSelectedActivitiesTypes()

  Column(modifier = Modifier.fillMaxSize().testTag("filterScreen")) {
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

    LazyColumn(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f).padding(16.dp),
        horizontalAlignment = Alignment.Start) {
          item { Spacer(modifier = Modifier.fillMaxHeight(0.025f)) }

          // Filter by activity type
          item {
            Column {
              Text(
                  text = stringResource(id = R.string.filter_activity_type),
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.Bold)

              FlowRow(
                  verticalArrangement = Arrangement.spacedBy(4.dp),
                  horizontalArrangement = Arrangement.SpaceEvenly,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp).fillMaxWidth()) {
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
          }

          item { Spacer(modifier = Modifier.fillMaxHeight(0.05f)) }

          // Filter by difficulty level
          item {
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
          }
          item { Spacer(modifier = Modifier.fillMaxHeight(0.05f)) }

          item {
            // Enable the user to see or not his/her completed activities
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { discoverScreenCallBacks.setShowCompleted(it) },
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
          }
        }
    Column(modifier = Modifier.fillMaxSize()) {
      HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f))

      Row(
          modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(horizontal = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Button(
            onClick = {
              // go back to snapshot values
              activitiesLevelArray[0] = userSelectedLevels.climbingLevel
              activitiesLevelArray[1] = userSelectedLevels.hikingLevel
              activitiesLevelArray[2] = userSelectedLevels.bikingLevel
              selectedActivitiesTypes.clear()
              selectedActivitiesTypes.addAll(snapshotSelectedActivitiesTypes)
            },
            modifier = Modifier.testTag("EraseButton"),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))) {
              Text(
                  stringResource(id = R.string.erase_options),
                  style = MaterialTheme.typography.headlineSmall)
            }
        // Apply the filter options
        ElevatedButton(
            onClick = {
              discoverScreenCallBacks.setSelectedLevels(
                  UserActivitiesLevel(
                      activitiesLevelArray[0], activitiesLevelArray[1], activitiesLevelArray[2]))
              discoverScreenCallBacks.setSelectedActivitiesType(selectedActivitiesTypes.toList())
              navigateBack()
            },
            elevation = ButtonDefaults.elevatedButtonElevation(3.dp),
            modifier = Modifier.testTag("applyFilterOptionsButton").weight(0.6f),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
              Text(
                  LocalContext.current.getString(R.string.apply),
                  style = MaterialTheme.typography.headlineMedium)
            }
      }
    }
  }
}
