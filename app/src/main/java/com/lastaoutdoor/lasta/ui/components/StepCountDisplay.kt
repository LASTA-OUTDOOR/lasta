package com.lastaoutdoor.lasta.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue

// Remark : you can get the step counter sensor from the sensor manager using the following code:
// val stepCounterSensor =
// context.getSystemService(Context.SENSOR_SERVICE).getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
@Composable
fun StepCountDisplay(stepCounterSensor: Sensor?, modifier: Modifier) {

  // internal mutable variables to keep track of the current step count

  // this is the step count of the app, that is displayed and is set to zero when the composable is
  // first created
  var stepCount by remember { mutableIntStateOf(0) }
  // this is the global step count, which is the total number of steps taken today measured by the
  // device
  var globalStepCount by remember { mutableIntStateOf(0) }
  var textState by remember { mutableStateOf(TextFieldValue(stepCount.toString())) }

  // this variable is used to keep track of whether this is the first time the listener is called
  var isFirst = true

  // internal helper function to update the step count, created to avoid code duplication
  fun updateStepCount(newStepCount: Int) {
    // if this is the first time the listener is called, predict the big increment of the global
    // step count
    if (isFirst) {
      globalStepCount = -newStepCount
      isFirst = false
    }
    // update the global step count and the step count of the app
    globalStepCount += newStepCount - stepCount
    stepCount = newStepCount
    // update the text field with the new step count
    textState = TextFieldValue((stepCount - globalStepCount).toString())
  }

  val context = LocalContext.current
  val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

  var sensorEventListener: SensorEventListener? = null

  val permissionLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
          // Permission is granted, register sensor listener
          sensorEventListener =
              registerSensorListener(sensorManager, stepCounterSensor) { updateStepCount(it) }
        }
        // if the user does not grant the permission, the sensorEventListener will not be registered
      }

  if (stepCounterSensor == null) {
    // Step counter sensor not available
    UnavailableStepCounter(modifier)
  } else {
    // Step counter sensor available
    DisposableEffect(key1 = stepCounterSensor) {
      if (context.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) ==
          PackageManager.PERMISSION_GRANTED) {
        // Permission is already granted, register sensor listener
        registerSensorListener(sensorManager, stepCounterSensor) { updateStepCount(it) }
        stepCount = 0
      } else {
        // Permission is not granted, request it
        permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
      }
      onDispose { sensorManager.unregisterListener(sensorEventListener) }
    }
    Modifier.testTag("StepCountDisplayTag")
    StepsTakenField(globalStepCount, modifier)
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

fun registerSensorListener(
    sensorManager: SensorManager?,
    sensor: Sensor?,
    onStepCountChanged: (Int) -> Unit
): SensorEventListener {
  val sensorEventListener =
      object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
          if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val stepCount = event.values[0].toInt()
            onStepCountChanged(stepCount)
          }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
          // Do nothing
        }
      }
  sensorManager?.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
  return sensorEventListener
}
