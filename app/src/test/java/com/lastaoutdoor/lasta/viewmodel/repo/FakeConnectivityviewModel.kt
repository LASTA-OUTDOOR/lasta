package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.repository.app.ConnectivityRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeConnectivityviewRepo() : ConnectivityRepository {
  override val connectionState: Flow<ConnectionState> = flowOf(ConnectionState.CONNECTED)
}
