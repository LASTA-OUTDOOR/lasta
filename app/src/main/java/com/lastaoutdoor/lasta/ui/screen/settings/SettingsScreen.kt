package com.lastaoutdoor.lasta.ui.screen.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.SettingsType
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
    signOutAndNavigate: () -> Unit,
    deleteAccountAndNavigate: () -> Unit
) {

  val showDeleteDialog = remember { mutableStateOf(false) }

  if (showDeleteDialog.value) {
    AlertDialog(
        modifier = Modifier.testTag("settingsDeleteDialog"),
        onDismissRequest = { showDeleteDialog.value = false },
        title = { Text(text = LocalContext.current.getString(R.string.delete_account)) },
        text = { Text(text = LocalContext.current.getString(R.string.delete_account_prompt)) },
        confirmButton = {
          Button(
              onClick = {
                showDeleteDialog.value = false
                deleteAccountAndNavigate()
              },
              colors =
                  ButtonDefaults.buttonColors(
                      containerColor = MaterialTheme.colorScheme.error,
                      contentColor = MaterialTheme.colorScheme.onError),
              modifier = Modifier.testTag("settingsDeleteButton")) {
                Text(LocalContext.current.getString(R.string.yes))
              }
        },
        dismissButton = {
          Button(onClick = { showDeleteDialog.value = false }) {
            Text(LocalContext.current.getString(R.string.no))
          }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false))
  }

  LazyColumn(
      modifier =
          Modifier.fillMaxSize()
              .padding(horizontal = 16.dp, vertical = 8.dp)
              .testTag("settingsScreen"),
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally) {
        item {
          MediumTopAppBar(
              modifier = Modifier.fillMaxWidth().testTag("settingsAppBar"),
              title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                      TitleComponent(setUpOrSetting = false)
                    }
              },
              navigationIcon = {
                IconButton(onClick = { navigateBack() }) {
                  Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
              })
        }

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
        }

        item {
          SettingsHeader(setUpOrSetting = SettingsType.ACCOUNT)

          Spacer(modifier = Modifier.height(24.dp))

          FlowRow(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { signOutAndNavigate() },
                    modifier = Modifier.testTag("settingsSignOut")) {
                      Text(
                          text = LocalContext.current.getString(R.string.sign_out),
                          textAlign = TextAlign.Center)
                    }

                OutlinedButton(
                    modifier = Modifier.testTag("settingsDeleteAccount"),
                    onClick = { showDeleteDialog.value = true },
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)) {
                      Text(
                          text = LocalContext.current.getString(R.string.delete_account),
                          maxLines = 1,
                          textAlign = TextAlign.Center)
                    }

                Spacer(modifier = Modifier.height(24.dp))
              }
        }
      }
}
