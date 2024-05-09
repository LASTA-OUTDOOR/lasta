package com.lastaoutdoor.lasta.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.ui.components.DropDownMenuComponent
import com.lastaoutdoor.lasta.ui.components.SeparatorComponent

@Composable
fun SettingsComponent(
    language: Language,
    prefActivity: ActivityType,
    levels: UserActivitiesLevel,
    updateLanguage: (Language) -> Unit,
    updatePrefActivity: (ActivityType) -> Unit,
    updateClimbingLevel: (UserLevel) -> Unit,
    updateHikingLevel: (UserLevel) -> Unit,
    updateBikingLevel: (UserLevel) -> Unit,
) {
  var selectedLanguage = language
  var selectedActivity = prefActivity

  var climbingLevel = levels.climbingLevel
  var hikingLevel = levels.hikingLevel
  var bikingLevel = levels.bikingLevel

  // Title "Settings"
  Row {
    Text(
        text = LocalContext.current.getString(R.string.Account_settings),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.displayLarge,
        modifier = Modifier.testTag("settingsTitle"),
        textAlign = TextAlign.Center)
  }

  SeparatorComponent()

  Row {
    // Language selection
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
          text = LocalContext.current.getString(R.string.select_languague),
          style = MaterialTheme.typography.headlineMedium,
          color = MaterialTheme.colorScheme.onBackground)

      Spacer(modifier = Modifier.height(8.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        DropDownMenuComponent(
            items = Language.values().toList(),
            selectedItem = selectedLanguage,
            onItemSelected = { newLanguage: Language ->
              selectedLanguage = newLanguage
              updateLanguage(newLanguage)
            },
            toStr = { language -> language.resourcesToString(LocalContext.current) },
            fieldText = LocalContext.current.getString(R.string.languague),
            modifier = Modifier.testTag("settingsLanguage"))
      }
    }
  }

  Row {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      // Outdoor activity selection
      Text(
          text = LocalContext.current.getString(R.string.select_fav_activity),
          style = MaterialTheme.typography.headlineMedium,
          color = MaterialTheme.colorScheme.onBackground)

      Spacer(modifier = Modifier.height(8.dp))
      for (activity in ActivityType.values()) {
        val tag =
            when (activity) {
              ActivityType.CLIMBING -> "settingsClimbing"
              ActivityType.HIKING -> "settingsHiking"
              ActivityType.BIKING -> "settingsBiking"
            }
        Button(
            modifier = Modifier.padding(1.dp).testTag(tag),
            onClick = {
              selectedActivity = activity
              updatePrefActivity(activity)
            },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        if (selectedActivity == activity) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary)) {
              val sport =
                  when (activity) {
                    ActivityType.CLIMBING -> LocalContext.current.getString(R.string.climbing)
                    ActivityType.HIKING -> LocalContext.current.getString(R.string.hiking)
                    ActivityType.BIKING -> LocalContext.current.getString(R.string.biking)
                  }
              Text(text = sport)
            }
      }
    }
  }

  Row {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      for (activity in ActivityType.values()) {
        Text(
            text = activity.resourcesToString(LocalContext.current),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        DropDownMenuComponent(
            items = UserLevel.values().toList(),
            selectedItem =
                when (activity) {
                  ActivityType.CLIMBING -> climbingLevel
                  ActivityType.HIKING -> hikingLevel
                  ActivityType.BIKING -> bikingLevel
                },
            onItemSelected = { newLevel: UserLevel ->
              when (activity) {
                ActivityType.CLIMBING -> {
                  climbingLevel = newLevel
                  updateClimbingLevel(newLevel)
                }
                ActivityType.HIKING -> {
                  hikingLevel = newLevel
                  updateHikingLevel(newLevel)
                }
                ActivityType.BIKING -> {
                  bikingLevel = newLevel
                  updateBikingLevel(newLevel)
                }
              }
            },
            toStr = { level -> level.resourcesToString(LocalContext.current) },
            fieldText = LocalContext.current.getString(R.string.sport_level),
            modifier = Modifier.testTag("settings${activity.name}Level"))

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
