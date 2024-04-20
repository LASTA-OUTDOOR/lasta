package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.utils.ConnectionState
import kotlinx.coroutines.flow.Flow

interface ConnectivityRepository {
  val connectionState: Flow<ConnectionState>
}
