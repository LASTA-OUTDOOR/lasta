package com.lastaoutdoor.lasta.ui.screen.setup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.ui.components.DropDownMenuComponent

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SetupScreen(
    userId: String,
    language: Language,
    prefActivity: ActivityType,
    levels: UserActivitiesLevel,
    updateLanguage: (Language) -> Unit,
    updatePrefActivity: (ActivityType) -> Unit,
    updateClimbingLevel: (UserLevel) -> Unit,
    updateHikingLevel: (UserLevel) -> Unit,
    updateBikingLevel: (UserLevel) -> Unit,
    navigateToMain: () -> Unit,
) {
  // Put the node with tag "filterScreen" to be able to test it

  val languages = Language.values()

  var selectedLanguage = language
  var selectedActivity = prefActivity

  var selectedClimbingLevel = levels.climbingLevel
  var selectedHikingLevel = levels.hikingLevel
  var selectedBikingLevel = levels.bikingLevel

  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(horizontal = 20.dp, vertical = 50.dp)
              .testTag("setupScreen"),
      verticalArrangement = Arrangement.SpaceEvenly) {

        // Title "Settings"
        Row(
            modifier = Modifier.fillMaxWidth().testTag("setupTitle"),
            horizontalArrangement = Arrangement.Center) {
              Text(
                  text = LocalContext.current.getString(R.string.setup_title),
                  fontWeight = FontWeight.Bold,
                  style = MaterialTheme.typography.displayLarge,
                  color = MaterialTheme.colorScheme.onBackground)
            }
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
            verticalArrangement = Arrangement.SpaceBetween) {
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = LocalContext.current.getString(R.string.select_languague),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground)
                    DropDownMenuComponent(
                        items = languages.toList(),
                        selectedItem = selectedLanguage,
                        onItemSelected = {
                          selectedLanguage = it
                          updateLanguage(it)
                        },
                        { selectedLanguage ->
                          selectedLanguage.resourcesToString(LocalContext.current)
                        },
                        fieldText = LocalContext.current.getString(R.string.languague),
                        modifier = Modifier.testTag("setupLanguage"))
                  }

              Column(
                  modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.SpaceBetween) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                          Text(
                              text = LocalContext.current.getString(R.string.select_fav_activity),
                              style = MaterialTheme.typography.headlineMedium,
                              color = MaterialTheme.colorScheme.onBackground)
                        }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                          Button(
                              onClick = {
                                selectedActivity = ActivityType.CLIMBING
                                updatePrefActivity(ActivityType.CLIMBING)
                              },
                              modifier = Modifier.padding(1.dp).testTag("setupClimbing"),
                              colors =
                                  ButtonDefaults.buttonColors(
                                      containerColor =
                                          if (ActivityType.CLIMBING == selectedActivity)
                                              MaterialTheme.colorScheme.primary
                                          else MaterialTheme.colorScheme.secondaryContainer,
                                      contentColor = MaterialTheme.colorScheme.onPrimary)) {
                                Text(text = LocalContext.current.getString(R.string.climbing))
                              }
                          Button(
                              onClick = {
                                selectedActivity = ActivityType.HIKING
                                updatePrefActivity(ActivityType.HIKING)
                              },
                              modifier = Modifier.padding(1.dp).testTag("setupHiking"),
                              colors =
                                  ButtonDefaults.buttonColors(
                                      containerColor =
                                          if (ActivityType.HIKING == selectedActivity)
                                              MaterialTheme.colorScheme.primary
                                          else MaterialTheme.colorScheme.secondaryContainer,
                                      contentColor = MaterialTheme.colorScheme.onPrimary)) {
                                Text(text = LocalContext.current.getString(R.string.hiking))
                              }
                          Button(
                              onClick = {
                                selectedActivity = ActivityType.BIKING
                                updatePrefActivity(ActivityType.BIKING)
                              },
                              modifier = Modifier.padding(1.dp).testTag("setupBiking"),
                              colors =
                                  ButtonDefaults.buttonColors(
                                      containerColor =
                                          if (ActivityType.BIKING == selectedActivity)
                                              MaterialTheme.colorScheme.primary
                                          else MaterialTheme.colorScheme.secondaryContainer,
                                      contentColor = MaterialTheme.colorScheme.onPrimary)) {
                                Text(text = LocalContext.current.getString(R.string.biking))
                              }
                        }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                          val twoFirstActivities = ActivityType.values().take(2)
                          for (activity in twoFirstActivities) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                              var stringResource =
                                  if (activity == ActivityType.CLIMBING) R.string.climbing
                                  else R.string.hiking
                              Text(
                                  color = MaterialTheme.colorScheme.primary,
                                  fontWeight = FontWeight.Bold,
                                  style = MaterialTheme.typography.headlineMedium,
                                  text = LocalContext.current.getString(stringResource))
                              Spacer(modifier = Modifier.height(10.dp))
                              DropDownMenuComponent(
                                  items = UserLevel.values().toList(),
                                  selectedItem =
                                      if (activity == ActivityType.CLIMBING) selectedClimbingLevel
                                      else selectedHikingLevel,
                                  onItemSelected = { newLevel: UserLevel ->
                                    if (activity == ActivityType.CLIMBING) {
                                      selectedClimbingLevel = newLevel
                                      updateClimbingLevel(newLevel)
                                    } else {
                                      selectedHikingLevel = newLevel
                                      updateHikingLevel(newLevel)
                                    }
                                  },
                                  toStr = { it.toString() },
                                  fieldText = LocalContext.current.getString(R.string.sport_level),
                                  modifier = Modifier.testTag("setup${activity.name}Level"))
                            }
                          }
                        }

                    Spacer(modifier = Modifier.height(25.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                          Text(
                              color = MaterialTheme.colorScheme.primary,
                              fontWeight = FontWeight.Bold,
                              style = MaterialTheme.typography.headlineMedium,
                              text = LocalContext.current.getString(R.string.biking))
                          Spacer(modifier = Modifier.height(10.dp))
                          DropDownMenuComponent(
                              items = UserLevel.values().toList(),
                              selectedItem = selectedBikingLevel,
                              onItemSelected = { newLevel: UserLevel ->
                                selectedBikingLevel = newLevel
                                updateBikingLevel(newLevel)
                              },
                              toStr = { it.toString() },
                              fieldText = LocalContext.current.getString(R.string.sport_level),
                              modifier = Modifier.testTag("setupBikingLevel"))
                        }
                  }

              Box(contentAlignment = Alignment.Center) {
                Button(
                    modifier = Modifier.testTag("setupFinishButton"),
                    onClick = { navigateToMain() },
                ) {
                  Text(text = LocalContext.current.getString(R.string.finish))
                }
              }
            }
      }
}
