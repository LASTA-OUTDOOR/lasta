package com.lastaoutdoor.lasta.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun PermissionManager(updatePermission: (Boolean) -> Unit) {
  requestNotificationPermission(LocalContext.current, LocalContext.current as Activity)
  LocationPermissionManager(updatePermission)
}

// Composable asking user for permissions to access location
// @param viewModel: the viewmodel that will be updated with the permission status
@Composable
private fun LocationPermissionManager(updatePermission: (Boolean) -> Unit) {
  // Permission for geo-location
  val requestPermissionLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
          permissions ->
        when {
          permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
            // Precise location access granted.
            updatePermission(true)
          }
          permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
            // Only approximate location access granted.
            updatePermission(false) // this is not enough for google map to work properly
          }
          else -> {
            // No location access granted.
            updatePermission(false)
          }
        }
      }

  LaunchedEffect(Unit) {
    requestPermissionLauncher.launch(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
  }
}

fun requestNotificationPermission(context: Context, activity: Activity) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    val hasPermission =
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED

    if (!hasPermission) {
      ActivityCompat.requestPermissions(
          activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
    }
  }
}
