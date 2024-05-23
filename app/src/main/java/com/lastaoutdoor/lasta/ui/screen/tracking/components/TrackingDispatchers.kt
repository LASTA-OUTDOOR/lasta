package com.lastaoutdoor.lasta.ui.screen.tracking.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.lastaoutdoor.lasta.viewmodel.TrackingState

@SuppressLint("MissingPermission")
@Composable
fun TrackingDispatchers(
    trackingState: TrackingState,
    locationCallback: LocationCallback,
    registerSensorListener: (SensorManager, Sensor?, (Int) -> Unit) -> SensorEventListener,
    updateStepCount: (Int) -> Unit
) {
  val context = LocalContext.current

  var sensorEventListener: SensorEventListener? = null

  val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
  val locationRequest = remember {
    LocationRequest.create().apply {
      interval = 1000
      fastestInterval = 1000
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
  }

  val permissionLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
          // Permission is granted, register sensor listener
          sensorEventListener =
              registerSensorListener(trackingState.sensorManager, trackingState.sensor) {
                updateStepCount(it)
              }
        }
      }

  DisposableEffect(Unit) {
    fusedLocationClient.requestLocationUpdates(
        locationRequest, locationCallback, context.mainLooper)

    onDispose { fusedLocationClient.removeLocationUpdates(locationCallback) }
  }

  DisposableEffect(key1 = trackingState.sensor) {
    if (trackingState.sensor != null) {
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
    }
    onDispose { trackingState.sensorManager.unregisterListener(sensorEventListener) }
  }
}
