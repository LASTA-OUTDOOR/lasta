package com.lastaoutdoor.lasta.ui.screen.setup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.lastaoutdoor.lasta.ui.screen.settings.SettingsComponent

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

  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(horizontal = 16.dp, vertical = 8.dp)
              .testTag("setupScreen"),
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

        Row(verticalAlignment = Alignment.CenterVertically) {
          Button(
              modifier = Modifier.testTag("setupFinishButton"),
              onClick = { navigateToMain() },
          ) {
            Text(text = LocalContext.current.getString(R.string.finish))
          }
        }
      }
}
