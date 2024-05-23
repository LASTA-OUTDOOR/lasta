package com.lastaoutdoor.lasta.ui.screen.tracking

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.services.StopwatchService
import com.lastaoutdoor.lasta.services.StopwatchServiceHelper
import com.lastaoutdoor.lasta.services.StopwatchState
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
    locationCallback: LocationCallback,
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
      locationCallback = locationCallback,
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
          TrackingInfo(Modifier.weight(0.5f), trackingState)
          Row(
              modifier = Modifier.weight(0.1f).fillMaxSize(),
              verticalAlignment = Alignment.Top,
              horizontalArrangement = Arrangement.Center) {
                Button(
                    modifier = Modifier.weight(1f).fillMaxHeight(0.8f),
                    onClick = {
                      StopwatchServiceHelper.triggerForegroundService(
                          context = context,
                          action =
                              if (currentState == StopwatchState.Started) ACTION_SERVICE_STOP
                              else ACTION_SERVICE_START)
                    },
                    colors =
                        ButtonDefaults.buttonColors(
                            backgroundColor =
                                if (currentState == StopwatchState.Started) Red else Blue,
                            contentColor = Color.White)) {
                      Text(
                          text =
                              if (currentState == StopwatchState.Started) "Stop"
                              else if ((currentState == StopwatchState.Stopped)) "Resume"
                              else "Start")
                    }
                Spacer(modifier = Modifier.width(30.dp))
                Button(
                    modifier = Modifier.weight(1f).fillMaxHeight(0.8f),
                    onClick = {
                      StopwatchServiceHelper.triggerForegroundService(
                          context = context, action = ACTION_SERVICE_CANCEL)
                    },
                    enabled = seconds != "00" && currentState != StopwatchState.Started,
                    colors = ButtonDefaults.buttonColors(Color.Gray)) {
                      Text(text = "Cancel")
                    }
              }
        }
  }
}
