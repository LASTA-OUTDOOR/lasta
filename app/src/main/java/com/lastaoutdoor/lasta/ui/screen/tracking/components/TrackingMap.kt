package com.lastaoutdoor.lasta.ui.screen.tracking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
  var isFollowingMarker by remember { mutableStateOf(true) }

  val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }
  val properties by remember { mutableStateOf(MapProperties(mapType = MapType.TERRAIN)) }

  var zoom by remember { mutableFloatStateOf(initialZoom) }

  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(currentPosition, initialZoom)
  }

  fun centerCamera() {
    isFollowingMarker = true
    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentPosition, zoom)
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
    IconButton(
        onClick = { centerCamera() },
        modifier =
            Modifier.size(48.dp)
                .offset(x = (-8).dp, y = 8.dp)
                .align(Alignment.TopEnd)
                .background(Color.White, CircleShape)) {
          Icon(
              painter = painterResource(id = R.drawable.center_location_icon),
              contentDescription = "My location",
              modifier = Modifier.size(32.dp),
              tint = if (isFollowingMarker) MaterialTheme.colorScheme.primary else Color.Gray)
        }
  }
}
