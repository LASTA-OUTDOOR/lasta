@file:Suppress("NAME_SHADOWING")

package com.lastaoutdoor.lasta.ui.screen.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.ui.components.DropDownMenuComponent
import com.lastaoutdoor.lasta.ui.screen.discover.components.ToggleButton
import com.lastaoutdoor.lasta.ui.theme.AccentGreen
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterScreen(
    selectedLevels: StateFlow<UserActivitiesLevel>,
    setSelectedLevels: (UserActivitiesLevel) -> Unit,
    selectedActivitiesType: StateFlow<List<ActivityType>>,
    setSelectedActivitiesType: (List<ActivityType>) -> Unit,
    navigateBack: () -> Unit
) {

  val activities = ActivityType.values()

  val selectedLevels = selectedLevels.collectAsState().value

  val activitiesLevelArray = remember { mutableStateListOf(
      selectedLevels.climbingLevel,
      selectedLevels.hikingLevel,
      selectedLevels.bikingLevel)
  }

    val selectedActivitiesTypes = remember { mutableStateListOf(selectedActivitiesType.value.first()) }

    var checkedBox by remember { mutableStateOf(true) }


  Column(
      modifier =
      Modifier
          .fillMaxSize()
          .testTag("filterScreen")) {
      MediumTopAppBar(
          title = {
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                  Text(
                      text = stringResource(id = R.string.filter_options),
                      style = MaterialTheme.typography.titleLarge,
                      color = MaterialTheme.colorScheme.onBackground
                  )
              }
          },
          navigationIcon = {
              IconButton(onClick = { navigateBack() }) {
                  Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
              }
          })

      HorizontalDivider(modifier = Modifier.fillMaxWidth())

      Spacer(modifier = Modifier.height(16.dp))

      // Filter by activity type
      Column(
          modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
      ) {
          Text(
              text = stringResource(id = R.string.filter_activity_type),
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.align(Alignment.Start)
          )

          Spacer(modifier = Modifier.height(16.dp))


          FlowRow(
              verticalArrangement = Arrangement.spacedBy(4.dp),
              horizontalArrangement = Arrangement.spacedBy(16.dp),
          ) {
              activities.forEach { activity ->
                  ToggleButton(activity.resourcesToString(LocalContext.current), isSelected = selectedActivitiesTypes.contains(activity)) {
                      if(!selectedActivitiesTypes.contains(activity)){
                          selectedActivitiesTypes.add(activity)
                      } else {
                          selectedActivitiesTypes.remove(activity)
                      }
                  }
              }

          }

          Spacer(modifier = Modifier.height(16.dp))

          // Filter by difficulty level
          Text(
              text = stringResource(id = R.string.filter_difficulty_level),
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.align(Alignment.Start)
          )

          Column(modifier = Modifier
              .padding(vertical = 8.dp, horizontal = 8.dp)
              .align(Alignment.Start)) {
              ActivityType.values().forEachIndexed { index, activityType ->
                  Row(modifier = Modifier.padding(vertical = 8.dp)){
                      val isEnabled = selectedActivitiesTypes.contains(activityType)
                      Button(
                          onClick = {},
                          enabled = isEnabled,
                          shape = MaterialTheme.shapes.small,
                          colors = ButtonDefaults.buttonColors(
                              containerColor = MaterialTheme.colorScheme.background,
                              contentColor = MaterialTheme.colorScheme.onBackground,
                              disabledContainerColor = Color.LightGray.copy(alpha = 0.6f),
                              disabledContentColor = Color.Black.copy(alpha = 0.3f),

                          ),
                          elevation = ButtonDefaults.elevatedButtonElevation(1.dp)){

                          DropDownMenuComponent(
                              items = UserLevel.values().toList(),
                              selectedItem = activitiesLevelArray[index],
                              onItemSelected = { activitiesLevelArray[index] = it},
                              toStr = { level -> level.resourcesToString(LocalContext.current) },
                              fieldText = activityType.resourcesToString(LocalContext.current),
                              isEnabled = isEnabled
                          )
                      }
                  }
              }
          }


          // Enable the user to see or not his/her completed activities
          Row(verticalAlignment = Alignment.CenterVertically) {
              Checkbox(
                  checked = checkedBox,
                  onCheckedChange = { checkedBox = it },
                  colors =
                  CheckboxDefaults.colors(uncheckedColor = AccentGreen, checkedColor = AccentGreen)
              )
              Text(
                  text = stringResource(id = R.string.show_completed_activities),
                  style =
                  TextStyle(
                      fontSize = 20.sp,
                      lineHeight = 24.sp,
                      fontWeight = FontWeight(500),
                      color = AccentGreen
                  )
              )
          }


          // Apply the filter options
          ElevatedButton(
              onClick = {
                  //setSelectedActivityType(activities[selectedIndex])
                  navigateBack()
              },
              modifier = Modifier
                  .width(305.dp)
                  .height(48.dp)
                  .testTag("applyFilterOptionsButton"),
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
          ) {
              Text(
                  LocalContext.current.getString(R.string.apply),
                  style =
                  TextStyle(
                      fontSize = 22.sp,
                      lineHeight = 28.sp,
                      fontWeight = FontWeight(400),
                  )
              )
          }
      }
  }
}
