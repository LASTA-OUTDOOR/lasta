package com.lastaoutdoor.lasta.view.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.lastaoutdoor.lasta.viewmodel.MapViewModel

// This composable will be used to manage the permissions for the map

@Composable
fun ManagePermissions(viewModel: MapViewModel) {
  // Permission for geo-location
  val requestPermissionLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
          permissions ->
        when {
          permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
            // Precise location access granted.
            viewModel.updatePermission(true)
          }
          permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
            // Only approximate location access granted.
            viewModel.updatePermission(true)
          }
          else -> {
            // No location access granted.
            viewModel.updatePermission(false)
          }
        }
      }

  LaunchedEffect(Unit) {
    requestPermissionLauncher.launch(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
  }
}

// This composable shows a map provided by Google Maps and will be used to show the activities
// locations
@SuppressLint("RestrictedApi", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen(
    viewModel: MapViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(), // viewModel for the MapScreen
) {

  // Initialise the map, otherwise the icon functionality won't work
  MapsInitializer.initialize(getApplicationContext())

  // Ask for permissions
  ManagePermissions(viewModel)

  // Initial values for testing the map implementation (will not stay once we can fetch activities)
  val lausanne =
      activityMarker(
          "Lausanne",
          LatLng(46.519962, 6.633597),
          "Example activity in Lausanne",
          "Lausanne",
          BitmapDescriptorFactory.fromResource(R.drawable.discover_icon))
  val zoom = 13f

  // camera that goes to the initial position and can be moved by the user
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(lausanne.position, zoom)
  }

  // Scaffold to display the map and a button to filter what is on it
  val scaffoldState = rememberScaffoldState()
  Scaffold(
      scaffoldState = scaffoldState,
      // If we want to add a filter button
      //      floatingActionButton = {
      //        FloatingActionButton(onClick = {  }) {
      //            Icon(imageVector = Icons.Default.Menu, contentDescription = "Filter")
      //        }
      //      }

  ) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = viewModel.state.properties,
        uiSettings = viewModel.state.uiSettings,
        cameraPositionState = cameraPositionState) {
          // Point on the map, we could have added multiple and changed more properties
          Marker(
              state = MarkerState(position = lausanne.position),
              title = lausanne.name,
              icon = lausanne.icon,
              snippet = lausanne.description)
        }
  }
}
