package com.lastaoutdoor.lasta.ui.screen.tracking

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.ui.screen.tracking.components.LocationsDisplay
import com.lastaoutdoor.lasta.ui.screen.tracking.components.StepCountDisplay
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround) {
          TrackingMap(
              if (trackingState.positions.isEmpty()) startPoint
              else LatLng(trackingState.positions.last().lat, trackingState.positions.last().lon),
              initialZoom,
            trackingState.positions.map { position -> LatLng(position.lat, position.lon) },
              Modifier.weight(0.6f))
          LocationsDisplay(Modifier.weight(0.3f), trackingState, locationCallback)
          StepCountDisplay(
              Modifier.weight(0.1f), trackingState, registerSensorListener, updateStepCount)
        }
  }
}
