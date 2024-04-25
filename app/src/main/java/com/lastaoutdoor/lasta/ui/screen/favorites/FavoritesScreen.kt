package com.lastaoutdoor.lasta.ui.screen.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.components.WeatherReport
import com.lastaoutdoor.lasta.ui.navigation.LeafScreen

@Composable
fun FavoritesScreen(
    navController: NavHostController,
) {
    val displayWeather = remember {
        mutableStateOf(false)
    }
    if(displayWeather.value){
        Dialog({displayWeather.value=false}){
            WeatherReport()
        }
    }

  Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { navController.navigate(LeafScreen.MoreInfo.route) }) {
          Text(text = LocalContext.current.getString(R.string.more_info))
        }
          Button(onClick = { displayWeather.value=true }) {
              Text(text = LocalContext.current.getString(R.string.weather_report))
          }
      }

}
