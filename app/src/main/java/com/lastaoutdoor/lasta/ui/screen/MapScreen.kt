package com.lastaoutdoor.lasta.ui.screen

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
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.map.ClimbingMarker
import com.lastaoutdoor.lasta.viewmodel.MapViewModel

// This composable will be used to manage the permissions for the map (This needs to be called
// inside a composable because of the rememberLauncherForActivityResult)
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
            viewModel.updatePermission(false) // this is not enough for google map to work properly
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
      ClimbingMarker(
          "Lausanne",
          LatLng(46.519962, 6.633597),
          "Example Hiking point in lausanne",
          BitmapDescriptorFactory.fromResource(R.drawable.discover_icon))
  val zoom = 11f

  // camera that goes to the initial position and can be moved by the user
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(lausanne.position, zoom)
  }

  // Refresh markers when the camera position changes (the launched effect is used to avoid calling
  // at every small movement))
  LaunchedEffect(cameraPositionState.isMoving) {
    if (!cameraPositionState.isMoving) {
      val centerLocation = cameraPositionState.position.target
      val topLeftLocation =
          cameraPositionState.projection?.visibleRegion?.farLeft
              ?: cameraPositionState.position.target
      val rad = SphericalUtil.computeDistanceBetween(centerLocation, topLeftLocation)
      viewModel.updateMarkers(centerLocation, rad)
    }
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
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
          val centerLocation = cameraPositionState.position.target
          val topLeftLocation =
              cameraPositionState.projection?.visibleRegion?.farLeft
                  ?: cameraPositionState.position.target
          val rad = SphericalUtil.computeDistanceBetween(centerLocation, topLeftLocation)
          viewModel.updateMarkers(centerLocation, rad)
        }) {

          // display all the markers fetched by the viewmodel
          viewModel.state.markerList.forEach { marker ->
            Marker(
                state = MarkerState(position = marker.position),
                title = marker.name,
                icon = marker.icon,
                snippet = marker.description)
          }

          //display all the itineraries fetched by the viewmodel
          viewModel.state.itineraryList.forEach { itinerary ->
            Polyline(
                points = itinerary.points
            )
          }
        }
  }
}
