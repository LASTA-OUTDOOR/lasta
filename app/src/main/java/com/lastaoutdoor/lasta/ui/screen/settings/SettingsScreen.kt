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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.ui.components.DropDownMenuComponent
import com.lastaoutdoor.lasta.ui.screen.moreinfo.TopBarLogo

@Composable
fun SettingsScreen(
    language: Language,
    prefActivity: ActivityType,
    updateLanguage: (Language) -> Unit,
    updatePrefActivity: (ActivityType) -> Unit,
    navigateBack: () -> Unit,
    signOutAndNavigate: () -> Unit
) {
  var selectedLanguage = language

  // If prefViewmodel pref sport is hiking set a called "isHiking" to true
  val isHiking = prefActivity == ActivityType.HIKING
  var selectedSport = isHiking

  TopBarLogo(logoPainterId = R.drawable.arrow_back) {
    // preferencesViewModel.updateLanguage(selectedLanguage)
    // database.updateFieldInUser(authViewModel.user!!.userId, "language", selectedLanguage)
    navigateBack()
  }

  Column(
      modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 120.dp),
      verticalArrangement = Arrangement.SpaceBetween) {

        // Title "Settings"
        Text(
            text = LocalContext.current.getString(R.string.Account_settings),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(40.dp))

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
                  toStr = { it.name },
                  fieldText = LocalContext.current.getString(R.string.languague))
            }

        Spacer(modifier = Modifier.height(40.dp))

        // Outdoor activity selection
        Text(
            text = LocalContext.current.getString(R.string.select_fav_activity),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground)

        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
              Button(
                  onClick = {
                    selectedSport = true
                    updatePrefActivity(ActivityType.HIKING)
                    // database.updateFieldInUser(authViewModel.user!!.userId, "prefSport",
                    // "Hiking")
                  },
                  modifier = Modifier.padding(16.dp),
                  colors =
                      if (selectedSport) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary)
                      } else {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondary)
                      }) {
                    Text(text = LocalContext.current.getString(R.string.hiking))
                  }
              Button(
                  onClick = {
                    selectedSport = false
                    updatePrefActivity(ActivityType.CLIMBING)
                    // database.updateFieldInUser(authViewModel.user!!.userId, "prefSport",
                    // "Climbing")
                  },
                  modifier = Modifier.padding(16.dp),
                  colors =
                      if (!selectedSport) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary)
                      } else {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondary)
                      }) {
                    Text(text = LocalContext.current.getString(R.string.climbing))
                  }
            }

        Spacer(modifier = Modifier.height(40.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { signOutAndNavigate() }) {
              Text(text = LocalContext.current.getString(R.string.sign_out))
            }

            Button(
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
