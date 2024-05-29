package com.lastaoutdoor.lasta.ui.screen.tracking

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.services.StopwatchService
import com.lastaoutdoor.lasta.services.StopwatchServiceHelper
import com.lastaoutdoor.lasta.services.StopwatchState
import com.lastaoutdoor.lasta.ui.screen.tracking.components.TrackingButtons
import com.lastaoutdoor.lasta.ui.screen.tracking.components.TrackingDispatchers
import com.lastaoutdoor.lasta.ui.screen.tracking.components.TrackingInfo
import com.lastaoutdoor.lasta.ui.screen.tracking.components.TrackingMap
import com.lastaoutdoor.lasta.utils.Constants.ACTION_SERVICE_CANCEL
import com.lastaoutdoor.lasta.utils.Constants.ACTION_SERVICE_START
import com.lastaoutdoor.lasta.utils.Constants.ACTION_SERVICE_STOP
import com.lastaoutdoor.lasta.viewmodel.TrackingState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TrackingScreen(
    stopwatchService: StopwatchService,
    trackingState: TrackingState,
    getLocationCallback: () -> LocationCallback,
    registerSensorListener: (SensorManager, Sensor?, (Int) -> Unit) -> SensorEventListener,
    updateStepCount: (Int) -> Unit
) {
  val context = LocalContext.current
  val hours by stopwatchService.hours
  val minutes by stopwatchService.minutes
  val seconds by stopwatchService.seconds
  val currentState by stopwatchService.currentState
  val startPoint = LatLng(46.519962, 6.633597)
  val initialZoom = 17f

  TrackingDispatchers(
      trackingState = trackingState,
      getLocationCallback = getLocationCallback,
      registerSensorListener = registerSensorListener,
      updateStepCount = updateStepCount)
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
          TrackingInfo(Modifier.weight(0.5f), trackingState, hours, minutes, seconds)
          TrackingButtons(
              currentState = currentState,
              cancelEnabled = seconds != "00" && currentState != StopwatchState.Started,
              onClickStart = {
                StopwatchServiceHelper.triggerForegroundService(
                    context = context,
                    action =
                        if (currentState == StopwatchState.Started) ACTION_SERVICE_STOP
                        else ACTION_SERVICE_START)
              },
              onClickCancel = {
                StopwatchServiceHelper.triggerForegroundService(
                    context = context, action = ACTION_SERVICE_CANCEL)
              },
              Modifier.weight(0.1f))
        }
  }
}
