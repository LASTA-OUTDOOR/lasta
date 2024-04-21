@file:OptIn(ExperimentalMaterial3Api::class)

package com.lastaoutdoor.lasta.ui.screen.map

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.lastaoutdoor.lasta.viewmodel.MapViewModel
import kotlinx.coroutines.launch

// Called after a click on a pointer on the map
// @param viewModel: the viewmodel
// @param sheetState: the state of the sheet
// @param isSheetOpen: whether the sheet is open or not
// @param onDismissRequest: function to close the sheet and update the state
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationSheet(
    viewModel: MapViewModel = hiltViewModel(),
    sheetState: SheetState,
    isSheetOpen: Boolean,
    onDismissRequest: () -> Unit
) {

  // We use the rememberSaveable to keep the state of the sheet (whether it is open or not)
  if (isSheetOpen) {
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
        modifier = Modifier.fillMaxSize().testTag("bottomSheet"),
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
            modifier = Modifier.testTag("bottomSheetTitle"),
            text = viewModel.state.selectedMarker?.name ?: "No name",
            style = MaterialTheme.typography.headlineLarge)
        Text(
            "Activity Type: ${viewModel.state.selectedMarker?.description ?: "Not specified"}",
            style = MaterialTheme.typography.bodyLarge)
      }
    }
  }
}

// Composable asking user for permissions to access location
// @param viewModel: the viewmodel that will be updated with the permission status
@Composable
private fun ManagePermissions(viewModel: MapViewModel = hiltViewModel()) {
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
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {

  // Initialise the map, otherwise the icon functionality won't work
  MapsInitializer.initialize(getApplicationContext())

  // Ask for permissions to determine what view to display
  ManagePermissions(viewModel)

  // camera that goes to the initial position and can be moved by the user
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(viewModel.initialPosition, viewModel.initialZoom)
  }

  // State used to manage the sheet (close it and open it)
  val sheetState = rememberModalBottomSheetState()
  var isSheetOpen by rememberSaveable { mutableStateOf(false) }

  InformationSheet(
      viewModel = viewModel,
      sheetState = sheetState,
      isSheetOpen = isSheetOpen,
      onDismissRequest = { isSheetOpen = false })

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

  // LaunchedEffect will draw the markers directly without a need to trigger the camera movement
  LaunchedEffect(Unit) { viewModel.updateMarkers(cameraPositionState.position.target, 5000.0) }

  // draw the map
  GoogleMapComposable(viewModel, cameraPositionState) { isSheetOpen = true }
}

// Composable that displays the Google Map
// @param viewModel: the viewmodel that will be used to fetch the activities and update the map
// @param cameraPositionState: the state of the camera
// @param updateSheet: function to update the sheet state (close it)
@Composable
private fun GoogleMapComposable(
    viewModel: MapViewModel = hiltViewModel(),
    cameraPositionState: CameraPositionState,
    updateSheet: () -> Unit,
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
      },
  ) {

    // TODO: find a working way to get these part out of the composable. (It blocks the
    // recomposition if I do it and cannot figure out why)

    // display all the classical markers fetched by the viewmodel
    viewModel.state.markerList.forEach { marker ->
      Marker(
          state = MarkerState(position = marker.position),
          title = marker.name,
          icon = BitmapDescriptorFactory.fromResource(marker.icon),
          snippet = marker.description,
          onClick = {
            updateSheet()
            viewModel.updateSelectedMarker(marker)
            viewModel.clearSelectedItinerary()
            // camera moves to the marker when clicked
            cameraPositionState.move(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(marker.position, viewModel.selectedZoom)))

            true
          },
      )
    }

    viewModel.state.itineraryStartMarkers.forEach { marker ->
      Marker(
          state = MarkerState(position = marker.position),
          title = marker.name,
          icon = BitmapDescriptorFactory.fromResource(marker.icon),
          snippet = marker.description,
          onClick = {
            updateSheet()
            viewModel.updateSelectedMarker(marker)
            viewModel.updateSelectedItinerary(marker.id)
            // camera moves to the marker when clicked
            cameraPositionState.move(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(marker.position, viewModel.selectedZoom)))
            true
          })
    }

    // display the itinerary if it is selected
    viewModel.state.selectedItinerary?.let {
      Polyline(
          points = it.points,
          width = 10f,
      )
    }
  }
}
