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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.theme.Black
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue

@Composable
fun StepCountDisplay() {
  var stepCount by remember { mutableIntStateOf(0) }
  var globalStepCount by remember { mutableIntStateOf(0) }
  var textState by remember { mutableStateOf(TextFieldValue(stepCount.toString())) }

  val context = LocalContext.current
  val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
  val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

  var isFirst = true

  var sensorEventListener: SensorEventListener? = null

  val permissionLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {

          // Permission is granted, register sensor listener
          sensorEventListener =
              registerSensorListener(sensorManager, stepCounterSensor) { newStepCount ->
                if (isFirst) {
                  globalStepCount = -newStepCount
                  isFirst = false
                }
                globalStepCount += newStepCount - stepCount
                stepCount = newStepCount
                textState = TextFieldValue((stepCount - globalStepCount).toString())
              }
        }
        //if the user does not grant the permission, the sensorEventListener will not be registered
      }

  if (stepCounterSensor == null) {
    // Step counter sensor not available
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
          Text(LocalContext.current.getString(R.string.step_count_unavailable))
          Text(LocalContext.current.getString(R.string.step_count_unsupported))
        }
  } else {
    // Step counter sensor available
    DisposableEffect(key1 = stepCounterSensor) {
      if (context.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) ==
          PackageManager.PERMISSION_GRANTED) {
        // Permission is already granted, register sensor listener
        registerSensorListener(sensorManager, stepCounterSensor) { newStepCount ->
            if (isFirst) {
                globalStepCount = -newStepCount
                isFirst = false
            }
          globalStepCount += newStepCount - stepCount
          stepCount = newStepCount
          textState = TextFieldValue((stepCount - globalStepCount).toString())
        }
        stepCount = 0
      } else {
        // Permission is not granted, request it
        permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
      }

      onDispose { sensorManager.unregisterListener(sensorEventListener) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Column{
            Text("Step Count", color = PrimaryBlue, modifier = Modifier.width(99.dp)
                .height(26.dp))
            Text(
                text = "$globalStepCount",
                color= Black,
                fontSize = 38.sp,
                fontWeight = FontWeight(600),
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        }
  }
}

private fun registerSensorListener(
    sensorManager: SensorManager,
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
                // Ignore
            }
        }
    sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    return sensorEventListener
}
