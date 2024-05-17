package com.lastaoutdoor.lasta.ui.screen.tracking

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.lastaoutdoor.lasta.ui.components.StepCountDisplay
import com.lastaoutdoor.lasta.ui.screen.tracking.components.LocationScreen

@Composable
fun TrackingScreen() {
  Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceAround) {
        LocationScreen()
        StepCountDisplay(
            stepCounterSensor =
                (LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
                    .getDefaultSensor(Sensor.TYPE_STEP_COUNTER))
      }
}
