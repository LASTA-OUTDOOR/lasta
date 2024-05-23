package com.lastaoutdoor.lasta.ui.screen.tracking

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.screen.tracking.components.TrackingInfo
import com.lastaoutdoor.lasta.ui.screen.tracking.components.TrackingMap
import com.lastaoutdoor.lasta.viewmodel.TrackingState

@Composable
fun TrackingScreen(
    trackingState: TrackingState,
    locationCallback: LocationCallback,
    registerSensorListener: (SensorManager, Sensor?, (Int) -> Unit) -> SensorEventListener,
    updateStepCount: (Int) -> Unit
) {
  val startPoint = LatLng(46.519962, 6.633597)
  val initialZoom = 17f
  Scaffold {
    Column(
        modifier = Modifier.padding(it).fillMaxSize().testTag("TrackingScreen"),
        horizontalAlignment = Alignment.CenterHorizontally) {
          TrackingMap(
              if (trackingState.positions.isEmpty()) startPoint
              else LatLng(trackingState.positions.last().lat, trackingState.positions.last().lon),
              initialZoom,
              trackingState.positions.map { position -> LatLng(position.lat, position.lon) },
              Modifier.weight(0.4f))
          //          LocationsDisplay(Modifier.weight(0.3f), trackingState, locationCallback)
          //          StepCountDisplay(
          //              Modifier.weight(0.2f), trackingState, registerSensorListener,
          // updateStepCount)
          TrackingInfo(Modifier.weight(0.5f))
          Row(
              modifier = Modifier.weight(0.1f).fillMaxSize(),
              verticalAlignment = Alignment.Top,
              horizontalArrangement = Arrangement.Center) {
                IconButton(
                    onClick = {},
                    modifier =
                        Modifier.size(64.dp)
                            .background(MaterialTheme.colorScheme.onBackground, CircleShape)) {
                      Icon(
                          imageVector = Icons.Default.PlayArrow,
                          contentDescription = "Pause button",
                          modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.background)
                    }
              }
        }
  }
}
