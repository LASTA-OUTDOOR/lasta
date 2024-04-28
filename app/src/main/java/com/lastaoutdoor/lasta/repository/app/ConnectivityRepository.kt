package com.lastaoutdoor.lasta.repository.app

import com.lastaoutdoor.lasta.utils.ConnectionState
import kotlinx.coroutines.flow.Flow

/** Repository to provide the current connection state of the device. */
interface ConnectivityRepository {
  /**
   * Provides the current connection state of the device. We use a [Flow] to emit the connection
   * state whenever it changes.
   */
  val connectionState: Flow<ConnectionState>
}
