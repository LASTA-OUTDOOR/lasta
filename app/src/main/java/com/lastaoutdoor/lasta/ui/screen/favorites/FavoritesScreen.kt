package com.lastaoutdoor.lasta.ui.screen.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.currentConnectivityState
import com.lastaoutdoor.lasta.utils.observeConnectivityAsFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FavoritesScreen() {

  val connection by connectivityState()

  val isConnected = connection === ConnectionState.CONNECTED

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(text = "$isConnected")
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
