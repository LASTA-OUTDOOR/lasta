package com.lastaoutdoor.lasta.ui.screen.setup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.ui.screen.settings.SettingsComponent
import com.lastaoutdoor.lasta.ui.screen.settings.TitleComponent

@OptIn(ExperimentalMaterial3Api::class)
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

  val showSubmitDialog = remember { mutableStateOf(false) }

  if (showSubmitDialog.value) {
    AlertDialog(
        modifier = Modifier.testTag("setupSubmitDialog"),
        onDismissRequest = { showSubmitDialog.value = false },
        title = { Text(text = LocalContext.current.getString(R.string.submit_settings)) },
        text = { Text(text = LocalContext.current.getString(R.string.submit_settings_prompt)) },
        confirmButton = {
          Button(
              onClick = {
                showSubmitDialog.value = false
                navigateToMain()
              },
              modifier = Modifier.testTag("setupSubmitButton")) {
                Text(text = LocalContext.current.getString(R.string.yes))
              }
        },
        dismissButton = {
          Button(onClick = { showSubmitDialog.value = false }) {
            Text(text = LocalContext.current.getString(R.string.no))
          }
        },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false))
  }

  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(horizontal = 16.dp, vertical = 8.dp)
              .testTag("setupScreen"),
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally) {
        MediumTopAppBar(
            title = {
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                TitleComponent(setUpOrSetting = true)
              }
            })
        LazyColumn {
          item {
            SettingsComponent(
                language = language,
                prefActivity = prefActivity,
                levels = levels,
                updateLanguage = updateLanguage,
                updatePrefActivity = updatePrefActivity,
                updateClimbingLevel = updateClimbingLevel,
                updateHikingLevel = updateHikingLevel,
                updateBikingLevel = updateBikingLevel)
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f))
          }

          item { Spacer(modifier = Modifier.height(16.dp)) }

          item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                  Button(
                      modifier = Modifier.testTag("setupFinishButton"),
                      onClick = { showSubmitDialog.value = true },
                  ) {
                    Text(text = LocalContext.current.getString(R.string.finish))
                  }
                }
          }
        }
      }
}
