package com.lastaoutdoor.lasta.ui.screen.tracking.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.lastaoutdoor.lasta.viewmodel.TrackingState

@SuppressLint("MissingPermission")
@Composable
fun LocationsDisplay(
    modifier: Modifier,
    trackingState: TrackingState,
    locationCallback: LocationCallback
) {
  val context = LocalContext.current

  val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
  val locationRequest = remember {
    LocationRequest.create().apply {
      interval = 1500
      fastestInterval = 1500
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
  }

  DisposableEffect(Unit) {
    fusedLocationClient.requestLocationUpdates(
        locationRequest, locationCallback, context.mainLooper)

    onDispose { fusedLocationClient.removeLocationUpdates(locationCallback) }
  }

  Box(contentAlignment = Alignment.Center, modifier = modifier.testTag("LocationsDisplay")) {
    // Display LazyColumn with the list of positions reversed
    LazyColumn {
      trackingState.positions.reversed().forEachIndexed { index, position ->
        item {
          Text(
              text =
                  "${trackingState.positions.size - index - 1}: (${position.lat}, ${position.lon})")
        }
      }
    }
  }
}
