package com.lastaoutdoor.lasta.ui.screen.tracking.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.utils.getScaledBitmapDescriptor

@Composable
fun TrackingMap(startPosition: LatLng, initialZoom: Float, positions: List<LatLng>, modifier: Modifier = Modifier) {
  var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }
  var properties by remember { mutableStateOf(MapProperties(mapType = MapType.TERRAIN)) }

  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(startPosition, initialZoom)
  }

  LaunchedEffect(key1 = startPosition) {
    //cameraPositionState.position = CameraPosition.fromLatLngZoom(startPosition, initialZoom)
  }

  Box(modifier.fillMaxSize()) {
    GoogleMap(
        modifier = Modifier.matchParentSize(),
        properties = properties,
        uiSettings = uiSettings,
        cameraPositionState = cameraPositionState) {
          Marker(
              state = MarkerState(position = startPosition),
              title = "My position",
              icon =
                  getScaledBitmapDescriptor(
                      LocalContext.current, R.drawable.location_arrow, 125, 125),
              anchor = Offset(0.5f, 0.5f)
          )
      Polyline(
        points = positions,
        color = Color.Blue,
        width = 12f,
      )
        }
  }
}
