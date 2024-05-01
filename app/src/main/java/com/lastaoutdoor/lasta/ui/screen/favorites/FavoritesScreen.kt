package com.lastaoutdoor.lasta.ui.screen.favorites

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.ui.components.WeatherReportBig

@Composable
fun FavoritesScreen(navigateToMoreInfo: () -> Unit, weather: WeatherResponse?) {

  Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { navigateToMoreInfo() }) {
            Text(text = LocalContext.current.getString(R.string.more_info))
        }
      }
}
