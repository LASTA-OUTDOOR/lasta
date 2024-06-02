package com.lastaoutdoor.lasta.viewmodel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.lastaoutdoor.lasta.models.api.Position
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class TrackingViewModel @Inject constructor(sensorManager: SensorManager) : ViewModel() {

  private val _state =
      MutableStateFlow(
          TrackingState(sensorManager, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)))
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

  /** Register a sensor listener to listen to the step counter sensor */
  fun registerSensorListener(
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
            // Do nothing
          }
        }
    sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    return sensorEventListener
  }

  fun getLocationCallBack(): LocationCallback {
    return object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult) {
        val location = locationResult.lastLocation
        location?.let {
          // Add the new position to the list of positions at the end
          _state.value =
              _state.value.copy(
                  positions = _state.value.positions.plus(Position(it.latitude, it.longitude)))
          if (_state.value.positions.size > 1) {
            val distance =
                SphericalUtil.computeDistanceBetween(
                    LatLng(_state.value.positions.last().lat, _state.value.positions.last().lon),
                    LatLng(
                        _state.value.positions[_state.value.positions.size - 2].lat,
                        _state.value.positions[_state.value.positions.size - 2].lon))
            _state.value = _state.value.copy(distanceDone = _state.value.distanceDone + distance)
          }
        }
      }
    }
  }
}

data class TrackingState(
    val sensorManager: SensorManager,
    val sensor: Sensor? = null,
    val stepCount: Int = 0,
    val positions: List<Position> = emptyList(),
    val distanceDone: Double = 0.0
)
