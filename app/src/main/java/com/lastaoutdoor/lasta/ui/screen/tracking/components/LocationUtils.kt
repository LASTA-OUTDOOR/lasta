package com.lastaoutdoor.lasta.ui.screen.tracking.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.lastaoutdoor.lasta.models.api.Position

@SuppressLint("MissingPermission")
@Composable
fun LocationScreen(modifier: Modifier) {
  val context = LocalContext.current

  // Define positions as a list of Position objects, initially empty
  val positions = remember { mutableStateListOf<Position>() }
  val distances = remember { mutableStateListOf<Double>() }

  val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
  val locationRequest = remember {
    LocationRequest.create().apply {
      interval = 3000
      fastestInterval = 3000
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
  }

  DisposableEffect(Unit) {
    val locationCallback =
        object : LocationCallback() {
          override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            location?.let {
              // Add the new position to the list of positions at the end
              positions.add(Position(it.latitude, it.longitude))
              if (positions.size > 1) {
                val distance = SphericalUtil.computeDistanceBetween(LatLng(positions.last().lat, positions.last().lon), LatLng(positions[positions.size - 2].lat, positions[positions.size - 2].lon))
                distances.add(distance)
              }
            }
          }
        }

    fusedLocationClient.requestLocationUpdates(
        locationRequest, locationCallback, context.mainLooper)

    onDispose { fusedLocationClient.removeLocationUpdates(locationCallback) }
  }

  Box(contentAlignment = Alignment.Center, modifier = modifier) {
    // Display LazyColumn with the list of positions reversed
    LazyColumn {
      positions.reversed().forEachIndexed { index, position ->
        item { Text(text = "${positions.size - index - 1}: (${position.lat}, ${position.lon})") }
      }
    }
  }
}
