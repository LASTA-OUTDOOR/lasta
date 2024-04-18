package com.lastaoutdoor.lasta.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/** Network utility to get current state of internet connection */
val Context.currentConnectivityState: ConnectionState
  get() {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return getCurrentConnectivityState(connectivityManager)
  }

private fun getCurrentConnectivityState(connectivityManager: ConnectivityManager): ConnectionState {
  val connected =
      connectivityManager.allNetworks.any { network ->
        connectivityManager
            .getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
      }

  return if (connected) ConnectionState.CONNECTED else ConnectionState.OFFLINE
}

/** Network Utility to observe availability or unavailability of Internet connection */
@ExperimentalCoroutinesApi
fun Context.observeConnectivityAsFlow() = callbackFlow {
  val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  val callback = NetworkCallback { connectionState -> trySend(connectionState) }

  val networkRequest =
      NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()

  connectivityManager.registerNetworkCallback(networkRequest, callback)

  // Set current state
  val currentState = getCurrentConnectivityState(connectivityManager)
  trySend(currentState)

  // Remove callback when not used
  awaitClose {
    // Remove listeners
    connectivityManager.unregisterNetworkCallback(callback)
  }
}

fun NetworkCallback(callback: (ConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
  return object : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
      callback(ConnectionState.CONNECTED)
    }

    override fun onLost(network: Network) {
      callback(ConnectionState.OFFLINE)
    }
  }
}
