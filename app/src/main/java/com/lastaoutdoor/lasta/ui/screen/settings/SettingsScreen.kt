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
import com.lastaoutdoor.lasta.ui.screen.moreinfo.TopBarLogo

@Composable
fun SettingsScreen(
    language: Language,
    prefActivity: ActivityType,
    levels: UserActivitiesLevel,
    updateLanguage: (Language) -> Unit,
    updatePrefActivity: (ActivityType) -> Unit,
    updateClimbingLevel: (UserLevel) -> Unit,
    updateHikingLevel: (UserLevel) -> Unit,
    updateBikingLevel: (UserLevel) -> Unit,
    navigateBack: () -> Unit,
    signOutAndNavigate: () -> Unit
) {
  var selectedLanguage = language
  var selectedActivity = prefActivity

  var climbingLevel = levels.climbingLevel
  var hikingLevel = levels.hikingLevel
  var bikingLevel = levels.bikingLevel

  TopBarLogo(logoPainterId = R.drawable.arrow_back) { navigateBack() }

  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(horizontal = 20.dp, vertical = 40.dp)
              .testTag("settingsScreen"),
      verticalArrangement = Arrangement.SpaceEvenly) {

        // Title "Settings"
        Text(
            text = LocalContext.current.getString(R.string.Account_settings),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth().testTag("settingsTitle"),
            textAlign = TextAlign.Center)

        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 35.dp),
            verticalArrangement = Arrangement.SpaceBetween) {
              // Language selection
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = LocalContext.current.getString(R.string.select_languague),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground)
                    DropDownMenuComponent(
                        items = Language.values().toList(),
                        selectedItem = selectedLanguage,
                        onItemSelected = { newLanguage: Language ->
                          selectedLanguage = newLanguage
                          updateLanguage(newLanguage)
                        },
                        toStr = { it.toString() },
                        fieldText = LocalContext.current.getString(R.string.languague),
                        modifier = Modifier.testTag("settingsLanguage"))
                  }

              Column(
                  modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
                  horizontalAlignment = Alignment.CenterHorizontally) {
                    // Outdoor activity selection
                    Text(
                        text = LocalContext.current.getString(R.string.select_fav_activity),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
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
                                            if (selectedActivity == activity)
                                                MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimary)) {
                                  val sport =
                                      when (activity) {
                                        ActivityType.CLIMBING ->
                                            LocalContext.current.getString(R.string.climbing)
                                        ActivityType.HIKING ->
                                            LocalContext.current.getString(R.string.hiking)
                                        ActivityType.BIKING ->
                                            LocalContext.current.getString(R.string.biking)
                                      }
                                  Text(text = sport)
                                }
                          }
                        }
                  }
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceEvenly) {
                    // First two dropdown menus in the first row
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)) {
                          Text(
                              text = LocalContext.current.getString(R.string.climbing),
                              color = MaterialTheme.colorScheme.primary,
                              fontWeight = FontWeight.Bold,
                              style = MaterialTheme.typography.headlineMedium)
                          Spacer(modifier = Modifier.height(8.dp))
                          DropDownMenuComponent(
                              items = UserLevel.values().toList(),
                              selectedItem = climbingLevel,
                              onItemSelected = { newLevel: UserLevel ->
                                climbingLevel = newLevel
                                updateClimbingLevel(newLevel)
                              },
                              toStr = { it.toString() },
                              fieldText = LocalContext.current.getString(R.string.sport_level),
                              modifier = Modifier.testTag("settingsCLIMBINGLevel"))
                        }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)) {
                          Text(
                              text = LocalContext.current.getString(R.string.hiking),
                              color = MaterialTheme.colorScheme.primary,
                              fontWeight = FontWeight.Bold,
                              style = MaterialTheme.typography.headlineMedium)
                          Spacer(modifier = Modifier.height(8.dp))
                          DropDownMenuComponent(
                              items = UserLevel.values().toList(),
                              selectedItem = hikingLevel,
                              onItemSelected = { newLevel: UserLevel ->
                                hikingLevel = newLevel
                                updateHikingLevel(newLevel)
                              },
                              toStr = { it.toString() },
                              fieldText = LocalContext.current.getString(R.string.sport_level),
                              modifier = Modifier.testTag("settingsHIKINGLevel"))
                        }
                  }
              Spacer(modifier = Modifier.height(10.dp))

              // Third dropdown menu in the second row
              Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = LocalContext.current.getString(R.string.biking),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    DropDownMenuComponent(
                        items = UserLevel.values().toList(),
                        selectedItem = bikingLevel,
                        onItemSelected = { newLevel: UserLevel ->
                          bikingLevel = newLevel
                          updateBikingLevel(newLevel)
                        },
                        toStr = { it.toString() },
                        fieldText = LocalContext.current.getString(R.string.sport_level),
                        modifier = Modifier.testTag("settingsBIKINGLevel"))
                  }
            }

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = { signOutAndNavigate() },
                modifier = Modifier.testTag("settingsSignOut")) {
                  Text(text = LocalContext.current.getString(R.string.sign_out))
                }

            Button(
                modifier = Modifier.testTag("settingsDeleteAccount"),
                onClick = { signOutAndNavigate() },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError)) {
                  Text(text = LocalContext.current.getString(R.string.delete_account))
                }
          }
        }
      }
}
