package com.lastaoutdoor.lasta.view.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// This composable shows a map provided by Google Maps and will be used to show the activities
// locations
@Composable
fun MapScreen() {

  // Initial values for testing the map implementation (will not stay once we can fetch activities)
  val lausanne = LatLng(46.519962, 6.633597)
  val zoom = 10f

  // The configuration part took some time, go to github wiki for more information

  // camera that goes to the initial position and can be moved by the user
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(lausanne, zoom)
  }

  GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState) {
    // Point on the map, we could have added multiple and changed more properties
    Marker(
        state = MarkerState(position = lausanne),
        title = "Lausanne",
        snippet = "Test marker in Lausanne")
  }
}
