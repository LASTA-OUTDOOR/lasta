package com.lastaoutdoor.lasta.data.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.lastaoutdoor.lasta.repository.app.ConnectivityRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

/**
 * Implementation of [ConnectivityRepository] to provide the current connection state of the device.
 */
class ConnectivityRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    ConnectivityRepository {
  private val connectivityManager: ConnectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  override val connectionState: Flow<ConnectionState> =
      callbackFlow {
            val connectivityCallback =
                object : ConnectivityManager.NetworkCallback() {
                  // This method is called when the network is available.
                  override fun onAvailable(network: Network) {
                    trySend(ConnectionState.CONNECTED)
                  }

                  // This method is called when the network timeouts.
                  override fun onUnavailable() {
                    trySend(ConnectionState.OFFLINE)
                  }

                  // This method is called when the network is lost.
                  override fun onLost(network: Network) {
                    trySend(ConnectionState.OFFLINE)
                  }
                }

            val request =
                NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .build()

            // We register the network callback to listen to the network changes.
            connectivityManager.registerNetworkCallback(request, connectivityCallback)

            // We remove the network callback when the flow is closed.
            awaitClose { connectivityManager.unregisterNetworkCallback(connectivityCallback) }
          }
          // Here we are filtering out the same connection state to avoid emitting the same state
          // multiple times.
          .distinctUntilChanged()
          .flowOn(Dispatchers.IO)
}
