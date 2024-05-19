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
import com.lastaoutdoor.lasta.ui.components.StepCountDisplay
import com.lastaoutdoor.lasta.ui.screen.tracking.components.LocationScreen
import com.lastaoutdoor.lasta.viewmodel.TrackingState

@Composable
fun TrackingScreen(trackingState: TrackingState, registerSensorListener: (SensorManager, Sensor?, (Int) -> Unit) -> SensorEventListener, updateStepCount: (Int) -> Unit) {
  Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceAround) {
        LocationScreen(Modifier.weight(0.7f))
        StepCountDisplay(Modifier.weight(0.3f), trackingState, registerSensorListener, updateStepCount)
      }
}
