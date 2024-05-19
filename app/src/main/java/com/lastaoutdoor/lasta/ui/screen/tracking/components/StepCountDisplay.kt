package com.lastaoutdoor.lasta.ui.screen.tracking.components

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import com.lastaoutdoor.lasta.viewmodel.TrackingState

@Composable
fun StepCountDisplay(
    modifier: Modifier,
    trackingState: TrackingState,
    registerSensorListener: (SensorManager, Sensor?, (Int) -> Unit) -> SensorEventListener,
    updateStepCount: (Int) -> Unit
) {

  var sensorEventListener: SensorEventListener? = null

  val context = LocalContext.current

  val permissionLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
          // Permission is granted, register sensor listener
          sensorEventListener =
              registerSensorListener(trackingState.sensorManager, trackingState.sensor) {
                updateStepCount(it)
              }
        }
        // if the user does not grant the permission, the sensorEventListener will not be registered
      }

  if (trackingState.sensor == null) {
    // Step counter sensor not available
    UnavailableStepCounter(modifier)
  } else {
    // Step counter sensor available
    DisposableEffect(key1 = trackingState.sensor) {
      if (context.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) ==
          PackageManager.PERMISSION_GRANTED) {
        // Permission is already granted, register sensor listener
        registerSensorListener(trackingState.sensorManager, trackingState.sensor) {
          updateStepCount(it)
        }
      } else {
        // Permission is not granted, request it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
      }
      onDispose { trackingState.sensorManager.unregisterListener(sensorEventListener) }
    }
    Modifier.testTag("StepCountDisplayTag")
    StepsTakenField(trackingState.stepCount, modifier)
  }
}

@Composable
fun UnavailableStepCounter(modifier: Modifier) {
  Column(
      modifier = modifier.fillMaxSize().testTag("UnavailableStepCounterTag"),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Text(LocalContext.current.getString(R.string.step_count_unavailable))
        Text(LocalContext.current.getString(R.string.step_count_unsupported))
      }
}

@Composable
fun StepsTakenField(globalStepCount: Int, modifier: Modifier) {
  Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier) {
        Text(
            LocalContext.current.getString(R.string.step_count),
            color = PrimaryBlue,
            modifier = Modifier.width(99.dp).height(26.dp).testTag("StepCountTag"))
        Text(
            text = "$globalStepCount",
            fontSize = 38.sp,
            fontWeight = FontWeight(600),
            modifier =
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp).testTag("GlobalStepCountTag"))
      }
}
