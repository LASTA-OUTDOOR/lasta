@file:OptIn(ExperimentalMaterial3Api::class)

package com.lastaoutdoor.lasta.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.lastaoutdoor.lasta.viewmodel.MapViewModel

// Called after a click on a pointer on the map
// @param viewModel: the viewmodel
@Composable
fun InformationSheet(viewModel: MapViewModel, sheetState: SheetState, isSheetOpen: Boolean, onDismissRequest: () -> Unit){

    if(isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { onDismissRequest() },
            sheetState = sheetState

        ) {
            Text("Test of the bottom sheet")
        }

    }

}

// Composable asking user for permissions to access location
// @param viewModel: the viewmodel that will be updated with the permission status
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

// This composable shows a map provided by Google Maps and will be used to show the activities and
// interact with them
// @param viewModel: the viewmodel that will be used to fetch the activities and update the map
@SuppressLint("RestrictedApi")
@Composable
fun MapScreen(
    viewModel: MapViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(), // viewModel for the MapScreen
) {

  // Initialise the map, otherwise the icon functionality won't work
  MapsInitializer.initialize(getApplicationContext())

  // Ask for permissions to determine what view to display
  ManagePermissions(viewModel)

  // camera that goes to the initial position and can be moved by the user
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(viewModel.initialPosition, viewModel.initialZoom)
  }

  val sheetState = rememberModalBottomSheetState()
  var isSheetOpen by rememberSaveable {
     mutableStateOf(false)
  }

  InformationSheet(viewModel = viewModel, sheetState = sheetState, isSheetOpen = isSheetOpen, onDismissRequest = {isSheetOpen = false})

  // Refresh markers when the camera position changes (the launched effect is used to avoid calling
  // at every small movement)
  LaunchedEffect(cameraPositionState.isMoving) {
    if (!cameraPositionState.isMoving) {

      // center coordinates of the map
      val centerLocation = cameraPositionState.position.target

      // top left coordinates of the map
      val topLeftLocation =
          cameraPositionState.projection?.visibleRegion?.farLeft
              ?: cameraPositionState.position.target

      // SphericalUtil is a utility class that provides methods to compute distances between two
      // points on sphere (due to the earth being a sphere)
      val rad = SphericalUtil.computeDistanceBetween(centerLocation, topLeftLocation)

      // update the markers based on the new center and radius
      viewModel.updateMarkers(centerLocation, rad)
    }
  }

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
      },
      onPOIClick = {
        isSheetOpen = true
      }
  ) {

        // display all the markers fetched by the viewmodel
        viewModel.state.markerList.forEach { marker ->
          Marker(
              state = MarkerState(position = marker.position),
              title = marker.name,
              icon = marker.icon,
              snippet = marker.description)
        }

        // display all the itineraries fetched by the viewmodel
        viewModel.state.itineraryList.forEach { itinerary -> Polyline(points = itinerary.points) }
      }
}
