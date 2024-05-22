package com.lastaoutdoor.lasta.ui.screen.tracking.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
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
fun TrackingMap(
    currentPosition: LatLng,
    initialZoom: Float,
    positions: List<LatLng>,
    modifier: Modifier = Modifier
) {
  var isFollowingMarker by remember {
    mutableStateOf(true)
  }

  val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }
  val properties by remember { mutableStateOf(MapProperties(mapType = MapType.TERRAIN)) }

  var zoom by remember { mutableFloatStateOf(initialZoom) }

  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(currentPosition, initialZoom)
  }

  LaunchedEffect(key1 = cameraPositionState.position.target) {
    if (cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
      isFollowingMarker = false
    }
  }

  LaunchedEffect(key1 = cameraPositionState.position.zoom) {
    zoom = cameraPositionState.position.zoom
  }

  LaunchedEffect(key1 = currentPosition) {
    if (isFollowingMarker) {
      cameraPositionState.position = CameraPosition.fromLatLngZoom(currentPosition, zoom)
    }
  }

  Box(modifier.fillMaxSize()) {
    GoogleMap(
        modifier = Modifier.matchParentSize(),
        properties = properties,
        uiSettings = uiSettings,
        cameraPositionState = cameraPositionState) {
          Marker(
              state = MarkerState(position = currentPosition),
              title = "My position",
              icon =
                  getScaledBitmapDescriptor(
                      LocalContext.current, R.drawable.location_arrow, 125, 125),
              anchor = Offset(0.5f, 0.5f))
          Polyline(
              points = positions,
              clickable = false,
              color = MaterialTheme.colorScheme.primary,
              width = 20f,
          )
        }
    Switch(checked = isFollowingMarker, onCheckedChange = { isFollowingMarker = it })
  }
}
