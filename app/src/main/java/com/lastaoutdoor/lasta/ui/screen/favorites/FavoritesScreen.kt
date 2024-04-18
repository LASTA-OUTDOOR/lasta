package com.lastaoutdoor.lasta.ui.screen.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.ui.navigation.LeafScreen
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.currentConnectivityState
import com.lastaoutdoor.lasta.utils.observeConnectivityAsFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FavoritesScreen(navController: NavHostController) {

  val connection by connectivityState()

  val isConnected = connection === ConnectionState.CONNECTED

  Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "$isConnected")
        Button(onClick = { navController.navigate(LeafScreen.MoreInfo.route) }) {
          Text(text = "More Info")
        }
      }
}

@ExperimentalCoroutinesApi
@Composable
fun connectivityState(): State<ConnectionState> {
  val context = LocalContext.current

  // Creates a State<ConnectionState> with current connectivity state as initial value
  return produceState(initialValue = context.currentConnectivityState) {
    // In a coroutine, can make suspend calls
    context.observeConnectivityAsFlow().collect { value = it }
  }
}
