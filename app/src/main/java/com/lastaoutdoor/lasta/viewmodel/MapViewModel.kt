package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

  var state by mutableStateOf(MapState())

  fun updatePermission(value: Boolean) {
    state.uiSettings = state.uiSettings.copy(myLocationButtonEnabled = value)
    state.properties = state.properties.copy(isMyLocationEnabled = value)
  }
}
