package com.lastaoutdoor.lasta.ui.screen.tracking

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.model.LatLng
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
  val initialZoom = 11f
  Scaffold {
    //    Column(
    //        modifier = Modifier.padding(it).fillMaxSize().testTag("TrackingScreen"),
    //        horizontalAlignment = Alignment.CenterHorizontally,
    //        verticalArrangement = Arrangement.SpaceAround) {
    //          LocationsDisplay(Modifier.weight(0.7f), trackingState, locationCallback)
    //          StepCountDisplay(
    //              Modifier.weight(0.3f), trackingState, registerSensorListener, updateStepCount)
    //        }
    TrackingMap(Modifier.padding(it), startPoint, initialZoom)
  }
}
