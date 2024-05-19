package com.lastaoutdoor.lasta.ui.screen.tracking

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.google.android.gms.location.LocationCallback
import com.lastaoutdoor.lasta.ui.screen.tracking.components.LocationsDisplay
import com.lastaoutdoor.lasta.ui.screen.tracking.components.StepCountDisplay
import com.lastaoutdoor.lasta.viewmodel.TrackingState

@Composable
fun TrackingScreen(
    trackingState: TrackingState,
    locationCallback: LocationCallback,
    registerSensorListener: (SensorManager, Sensor?, (Int) -> Unit) -> SensorEventListener,
    updateStepCount: (Int) -> Unit
) {
  Column(
      modifier = Modifier.fillMaxSize().testTag("TrackingScreen"),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceAround) {
        LocationsDisplay(Modifier.weight(0.7f), trackingState, locationCallback)
        StepCountDisplay(
            Modifier.weight(0.3f), trackingState, registerSensorListener, updateStepCount)
      }
}
