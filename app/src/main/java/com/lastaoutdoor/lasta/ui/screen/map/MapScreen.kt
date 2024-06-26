@file:OptIn(ExperimentalMaterial3Api::class)

package com.lastaoutdoor.lasta.ui.screen.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.map.MapItinerary
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.utils.getScaledBitmapDescriptor
import com.lastaoutdoor.lasta.viewmodel.MapState

// Called after a click on a pointer on the map
// @param viewModel: the viewmodel
// @param sheetState: the state of the sheet
// @param isSheetOpen: whether the sheet is open or not
// @param onDismissRequest: function to close the sheet and update the state
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationSheet(
    sheetState: SheetState,
    isSheetOpen: Boolean,
    selectedMarker: Marker?,
    onDismissRequest: () -> Unit
) {
  // We use the rememberSavable to keep the state of the sheet (whether it is open or not)
  if (isSheetOpen) {
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth().testTag("bottomSheet"),
        scrimColor = Color.Transparent) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier.testTag("bottomSheetTitle"),
                text = selectedMarker?.name ?: "No name",
                style = MaterialTheme.typography.headlineLarge)
            Text(
                text =
                    selectedMarker?.activity?.resourcesToString(LocalContext.current)
                        ?: LocalContext.current.getString(R.string.unknown_activity_type),
                style = MaterialTheme.typography.bodyLarge)
          }
        }
  }
}

// This composable shows a map provided by Google Maps and will be used to show the activities and
// interact with them
// @param viewModel: the viewmodel that will be used to fetch the activities and update the map
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RestrictedApi")
@Composable
fun mapScreen(
    state: MapState,
    initialPosition: LatLng,
    initialZoom: Float,
    updateSelectedMarker: (Marker) -> Unit,
    clearSelectedItinerary: () -> Unit,
    selectedZoom: Float,
    selectedMarker: Marker?,
    selectedItinerary: MapItinerary?,
    markerList: List<Marker>,
    clearSelectedMarker: () -> Unit,
    isRangeSearch: Boolean = false
): (CameraUpdate) -> Unit {

  // camera that goes to the initial position and can be moved by the user
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(initialPosition, initialZoom)
  }

  // State used to manage the sheet (close it and open it)
  val sheetState = rememberModalBottomSheetState()
  var isSheetOpen by rememberSaveable { mutableStateOf(false) }

  InformationSheet(
      sheetState = sheetState,
      isSheetOpen = isSheetOpen,
      selectedMarker = selectedMarker,
      onDismissRequest = { isSheetOpen = false })

  // draw the map
  GoogleMapComposable(
      cameraPositionState,
      state,
      updateSelectedMarker,
      selectedZoom,
      { isSheetOpen = true },
      selectedItinerary,
      markerList,
      selectedMarker,
      clearSelectedItinerary,
      clearSelectedMarker,
      isRangeSearch)

  // return a function that can move the camera to a specific position
  return cameraPositionState::move
}

// Composable that displays the Google Map
// @param viewModel: the viewmodel that will be used to fetch the activities and update the map
// @param cameraPositionState: the state of the camera
// @param updateSheet: function to update the sheet state (close it)
@Composable
private fun GoogleMapComposable(
    cameraPositionState: CameraPositionState,
    state: MapState,
    updateSelectedMarker: (Marker) -> Unit,
    selectedZoom: Float,
    updateSheet: () -> Unit,
    selectedItinerary: MapItinerary?,
    markerList: List<Marker>,
    selectedMarker: Marker?,
    clearSelectedItinerary: () -> Unit,
    clearSelectedMarker: () -> Unit,
    isRangeSearch: Boolean
) {

  GoogleMap(
      modifier = Modifier.fillMaxSize().testTag("googleMap"),
      properties = state.properties,
      uiSettings = state.uiSettings,
      cameraPositionState = cameraPositionState,
      onMapLoaded = {
        if (selectedMarker != null) {

          cameraPositionState.move(
              CameraUpdateFactory.newCameraPosition(
                  CameraPosition.fromLatLngZoom(selectedMarker.position, selectedZoom)))
          updateSheet()

          // updateSelectedMarker(null)
        }
        val centerLocation = cameraPositionState.position.target
        val topLeftLocation =
            cameraPositionState.projection?.visibleRegion?.farLeft
                ?: cameraPositionState.position.target
        val rad = SphericalUtil.computeDistanceBetween(centerLocation, topLeftLocation)
      },
  ) {

    // display the itinerary if it is selected
    if (selectedItinerary != null) {
      Polyline(
          points = selectedItinerary.points,
          color = Color.Blue,
          width = 12f,
      )
    }

    // display all the classical markers fetched by the viewmodel
    markerList.forEach { marker ->
      Marker(
          state = MarkerState(position = marker.position),
          title = marker.name,
          icon = getScaledBitmapDescriptor(LocalContext.current, marker.icon, 150, 150),
          snippet = marker.description,
          onClick = {
            if (!isRangeSearch && (selectedMarker == null || selectedMarker.id != marker.id)) {
              updateSelectedMarker(marker)
              updateSheet()
              // camera moves to the marker when clicked
              cameraPositionState.move(
                  CameraUpdateFactory.newCameraPosition(
                      CameraPosition.fromLatLngZoom(marker.position, selectedZoom)))
            } else {
              clearSelectedItinerary()
              clearSelectedMarker()
            }
            true
          },
      )
    }
  }
}
