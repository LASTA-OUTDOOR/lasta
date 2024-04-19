package com.lastaoutdoor.lasta.data.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.lastaoutdoor.lasta.repository.ConnectivityRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

class ConnectivityRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    ConnectivityRepository {
  private val connectivityManager: ConnectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  override val connectionState: Flow<ConnectionState> =
      callbackFlow {
            val connectivityCallback =
                object : ConnectivityManager.NetworkCallback() {
                  override fun onAvailable(network: Network) {
                    trySend(ConnectionState.CONNECTED)
                  }

                  override fun onUnavailable() {
                    trySend(ConnectionState.OFFLINE)
                  }

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

            connectivityManager.registerNetworkCallback(request, connectivityCallback)

            awaitClose { connectivityManager.unregisterNetworkCallback(connectivityCallback) }
          }
          .distinctUntilChanged()
          .flowOn(Dispatchers.IO)
}
