package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.repository.ConnectivityRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ConnectivityViewModel @Inject constructor(connectivityRepository: ConnectivityRepository) :
    ViewModel() {

  val connectionState: StateFlow<ConnectionState> =
      connectivityRepository.connectionState.stateIn(
          initialValue = ConnectionState.OFFLINE,
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000))
}