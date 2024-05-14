package com.lastaoutdoor.lasta.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    setUpOrSetting: String // "Setup" or "Settings"
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    TitleComponent(setUpOrSetting = setUpOrSetting) // Title of the screen
    Spacer(modifier = Modifier.height(24.dp))
    SettingsHeader(type = "General") // Header for General Settings
    Spacer(modifier = Modifier.height(24.dp))
    LanguageSelectionComponent(language, updateLanguage)
    Spacer(modifier = Modifier.height(24.dp))
    SettingsHeader(type = "Activity") // Header for Activity settings
    Spacer(modifier = Modifier.height(24.dp))
    FavoriteActivityComponent(prefActivity, updatePrefActivity) // Favorite Activity Selection
    Spacer(modifier = Modifier.height(24.dp))
    ActivityLevelsComponent(levels, updateClimbingLevel, updateHikingLevel, updateBikingLevel)
  }
}

@Composable
fun SettingsHeader(type: String) {

  Column {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
      Text(
          text =
              when (type) {
                "General" -> LocalContext.current.getString(R.string.general_settings)
                "Activity" -> LocalContext.current.getString(R.string.activity_settings)
                else -> {
                  LocalContext.current.getString(R.string.general_settings)
                }
              },
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary,
          style = MaterialTheme.typography.bodySmall,
          modifier = Modifier.padding(horizontal = 16.dp).testTag("settingsHeader"),
          textAlign = TextAlign.Justify)
    }
    Spacer(modifier = Modifier.height(8.dp))
  }
  HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f))
}

@Composable
fun TitleComponent(setUpOrSetting: String) {
  Row {
    if (setUpOrSetting == "Setup") {
      // put a small setting icon in secondary color
      Icon(
          imageVector = Icons.Default.Build,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.secondary,
          modifier = Modifier.padding(8.dp))
    } else {
      // put a small setting icon in secondary color
      Icon(
          imageVector = Icons.Default.Settings,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.secondary,
          modifier = Modifier.padding(8.dp))
    }
    Text(
        text =
            when (setUpOrSetting) {
              "Settings" -> LocalContext.current.getString(R.string.Account_settings)
              "Setup" -> LocalContext.current.getString(R.string.Account_setup)
              else -> LocalContext.current.getString(R.string.Account_settings)
            },
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.displayLarge,
        modifier = Modifier.testTag("settingsTitle"),
        textAlign = TextAlign.Center)
    if (setUpOrSetting == "Setup") {
      // put a small setting icon in secondary color
      Icon(
          imageVector = Icons.Default.Build,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.secondary,
          modifier = Modifier.padding(8.dp))
    } else {
      // put a small setting icon in secondary color
      Icon(
          imageVector = Icons.Default.Settings,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.secondary,
          modifier = Modifier.padding(8.dp))
    }
  }
}

@Composable
fun LanguageSelectionComponent(
    language: Language,
    updateLanguage: (Language) -> Unit,
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
        text = LocalContext.current.getString(R.string.select_languague),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground)
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        onClick = {},
        modifier = Modifier.testTag("languageDropDownButton"),
        enabled = true,
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
              items = Language.values().toList(),
              selectedItem = language,
              onItemSelected = { newLanguage: Language -> updateLanguage(newLanguage) },
              toStr = { language -> language.resourcesToString(LocalContext.current) },
              fieldText = LocalContext.current.getString(R.string.languague),
              modifier = Modifier.testTag("settingsLanguage"))
        }
  }
}

@Composable
fun FavoriteActivityComponent(
    prefActivity: ActivityType,
    updatePrefActivity: (ActivityType) -> Unit,
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
        text = LocalContext.current.getString(R.string.select_fav_activity),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground)
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.testTag("settingsFavActivity")) {
      for (activity in ActivityType.values()) {
        val tag = "settings${activity.name}"
        Button(
            modifier = Modifier.padding(1.dp).testTag(tag),
            onClick = { updatePrefActivity(activity) },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        if (prefActivity == activity) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary)) {
              Text(text = activity.resourcesToString(LocalContext.current))
            }
      }
    }
  }
}

@Composable
fun ActivityLevelsComponent(
    levels: UserActivitiesLevel,
    updateClimbingLevel: (UserLevel) -> Unit,
    updateHikingLevel: (UserLevel) -> Unit,
    updateBikingLevel: (UserLevel) -> Unit,
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
        text = LocalContext.current.getString(R.string.select_lev_activity),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground)
    Spacer(modifier = Modifier.height(12.dp))
    for (activity in ActivityType.values()) {
      val activityLevel =
          when (activity) {
            ActivityType.CLIMBING -> levels.climbingLevel
            ActivityType.HIKING -> levels.hikingLevel
            ActivityType.BIKING -> levels.bikingLevel
          }
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = activity.resourcesToString(LocalContext.current) + " :",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = {},
                modifier = Modifier.testTag("activityDropDownButton${activity.name}"),
                enabled = true,
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
                      selectedItem = activityLevel,
                      onItemSelected = { newLevel: UserLevel ->
                        when (activity) {
                          ActivityType.CLIMBING -> updateClimbingLevel(newLevel)
                          ActivityType.HIKING -> updateHikingLevel(newLevel)
                          ActivityType.BIKING -> updateBikingLevel(newLevel)
                        }
                      },
                      toStr = { level -> level.resourcesToString(LocalContext.current) },
                      fieldText = LocalContext.current.getString(R.string.sport_level),
                      modifier = Modifier.testTag("settings${activity.name}Level"))
                }
          }
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
