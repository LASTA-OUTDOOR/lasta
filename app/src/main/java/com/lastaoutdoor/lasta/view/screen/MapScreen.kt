package com.lastaoutdoor.lasta.view.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.model.data.activityMarker

// This composable will be used to manage the permissions for the map

// This composable shows a map provided by Google Maps and will be used to show the activities
// locations
@SuppressLint("RestrictedApi")
@Composable
fun MapScreen() {

  // Initialise the map, otherwise the icon functionnality won't work
  MapsInitializer.initialize(getApplicationContext())

  // Initial values for testing the map implementation (will not stay once we can fetch activities)
  val lausanne =
      activityMarker(
          "Lausanne",
          LatLng(46.519962, 6.633597),
          "Example activity in Lausanne",
          "Lausanne",
          BitmapDescriptorFactory.fromResource(R.drawable.discover_icon))
  val zoom = 10f

  // camera that goes to the initial position and can be moved by the user
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(lausanne.position, zoom)
  }

  GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState) {
    // Point on the map, we could have added multiple and changed more properties
    Marker(
        state = MarkerState(position = lausanne.position),
        title = lausanne.name,
        icon = lausanne.icon,
        snippet = lausanne.description)
  }
}
