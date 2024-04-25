package com.lastaoutdoor.lasta.ui.screen.discovery

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
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.ui.theme.AccentGreen
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavController) {
  var fromDistance by remember { mutableStateOf(0) }
  var toDistance by remember { mutableStateOf(1000) }

  Column(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally) {

        // return button to go back to the discovery screen
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.filter_options)) },
            navigationIcon = {
              IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
              }
            })

        Spacer(modifier = Modifier.height(16.dp))

        // Filter by activity type
        Text(
            text = stringResource(id = R.string.filter_activity_type),
            style = TextStyle(fontSize = 20.sp, lineHeight = 24.sp, fontWeight = FontWeight(500)))
        val activityTypes = listOf(ActivityType.HIKING, ActivityType.CLIMBING, ActivityType.BIKING)
        Row(verticalAlignment = Alignment.CenterVertically) {
          activityTypes.forEach { activityType ->
            RadioButton(
                selected = false, // TODO: Bind this to actual data
                onClick = { /*TODO*/})
            Text(text = activityType.toString())
          }
        }

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(thickness = 2.dp, color = PrimaryBlue)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.filter_difficulty_level),
            style = TextStyle(fontSize = 20.sp, lineHeight = 24.sp, fontWeight = FontWeight(500)))
        // TODO: Replace with your actual difficulty levels
        val difficultyLevels = listOf("Easy", "Medium", "Hard")
        Row(verticalAlignment = Alignment.CenterVertically) {
          difficultyLevels.forEach { difficultyLevel ->
            RadioButton(
                selected = false, // TODO: link with database
                onClick = { /*TODO*/})
            Text(text = difficultyLevel)
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
              // todo: save the filter options to be applied to the discovery screen
              navController.popBackStack()
            },
            modifier = Modifier.width(305.dp).height(48.dp).testTag("applyFilterOptionsButton"),
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
