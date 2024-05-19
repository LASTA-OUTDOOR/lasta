package com.lastaoutdoor.lasta.viewmodel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(sensorManager: SensorManager): ViewModel() {

  private val _state = MutableStateFlow(TrackingState(sensorManager, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)))
  val state: StateFlow<TrackingState> = _state

  private var isFirst = true
  private var globalStepCount = 0

  // internal helper function to update the step count, created to avoid code duplication
  fun updateStepCount(newStepCount: Int) {
    // if this is the first time the listener is called, predict the big increment of the global
    // step count
    if (isFirst) {
      globalStepCount = newStepCount
      isFirst = false
    }
    // update the global step count and the step count of the app
    _state.value = _state.value.copy(stepCount = newStepCount - globalStepCount)
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
}

data class TrackingState(
  val sensorManager: SensorManager,
  val sensor: Sensor? = null,
  val stepCount: Int = 0
)