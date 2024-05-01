package com.lastaoutdoor.lasta.ui.screen.setup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.ui.components.DropDownMenuComponent

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SetupScreen(
    language: Language,
    prefActivity: ActivityType,
    updateLanguage: (Language) -> Unit,
    updatePrefActivity: (ActivityType) -> Unit,
    navigateToMain: () -> Unit,
) {

  val languages = Language.values()

  var selectedLanguage by remember { mutableStateOf(language) }
  val outdoorActivities =
      listOf(
          LocalContext.current.getString(R.string.climbing),
          LocalContext.current.getString(R.string.hiking))
  var selectedPrefActivity by remember { mutableStateOf(prefActivity == ActivityType.HIKING) }

  Column(
      modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp, vertical = 190.dp),
      verticalArrangement = Arrangement.SpaceBetween) {

        // Title "Settings"
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
          Text(
              text = LocalContext.current.getString(R.string.setup_title),
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.displayLarge,
              color = MaterialTheme.colorScheme.onBackground)
        }

        Spacer(modifier = Modifier.height(50.dp))

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
                  onItemSelected = { selectedLanguage = it },
                  Language::toString,
                  fieldText = LocalContext.current.getString(R.string.languague))
            }
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
              Text(
                  text = LocalContext.current.getString(R.string.select_fav_activity),
                  style = MaterialTheme.typography.headlineMedium,
                  color = MaterialTheme.colorScheme.onBackground)
            }

        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
              Button(
                  onClick = { selectedPrefActivity = false },
                  modifier = Modifier.padding(16.dp),
                  colors =
                      if (!selectedPrefActivity) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary)
                      } else {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondary)
                      }) {
                    Text(text = outdoorActivities[1])
                  }
              Button(
                  onClick = { selectedPrefActivity = true },
                  modifier = Modifier.padding(16.dp),
                  colors =
                      if (selectedPrefActivity) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary)
                      } else {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondary)
                      }) {
                    Text(text = outdoorActivities[0])
                  }
            }

        Spacer(modifier = Modifier.height(40.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
          Button(
              onClick = {
                updateLanguage(selectedLanguage)
                val prefActivity =
                    if (selectedPrefActivity) ActivityType.HIKING else ActivityType.CLIMBING
                updatePrefActivity(prefActivity)
                navigateToMain()
              },
          ) {
            Text(text = LocalContext.current.getString(R.string.save))
          }
        }
      }
}
