package com.lastaoutdoor.lasta.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
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

  val showDeleteDialog = remember { mutableStateOf(false) }

  if (showDeleteDialog.value) {
    AlertDialog(
        onDismissRequest = { showDeleteDialog.value = false },
        title = { Text(text = LocalContext.current.getString(R.string.delete_account)) },
        text = { Text(text = "Are you sure you want to delete your account?") },
        confirmButton = {
          Button(
              onClick = {
                showDeleteDialog.value = false
                signOutAndNavigate()
              }) {
                Text("Yes")
              }
        },
        dismissButton = { Button(onClick = { showDeleteDialog.value = false }) { Text("No") } })
  }

  TopBarLogo(logoPainterId = R.drawable.arrow_back) { navigateBack() }

  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(horizontal = 16.dp, vertical = 8.dp)
              .testTag("settingsScreen"),
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally) {
        SettingsComponent(
            language = language,
            prefActivity = prefActivity,
            levels = levels,
            updateLanguage = updateLanguage,
            updatePrefActivity = updatePrefActivity,
            updateClimbingLevel = updateClimbingLevel,
            updateHikingLevel = updateHikingLevel,
            updateBikingLevel = updateBikingLevel)

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
          Button(
              onClick = { signOutAndNavigate() }, modifier = Modifier.testTag("settingsSignOut")) {
                Text(
                    text = LocalContext.current.getString(R.string.sign_out),
                    style = MaterialTheme.typography.headlineMedium)
              }

          Spacer(modifier = Modifier.width(16.dp))
          Button(
              modifier = Modifier.testTag("settingsDeleteAccount"),
              onClick = { showDeleteDialog.value = true },
              colors =
                  ButtonDefaults.buttonColors(
                      containerColor = MaterialTheme.colorScheme.error,
                      contentColor = MaterialTheme.colorScheme.onError)) {
                Text(
                    text = LocalContext.current.getString(R.string.delete_account),
                    style = MaterialTheme.typography.headlineMedium)
              }
        }
      }
}
