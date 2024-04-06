package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.db.ClimbingMarker
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository

class MapViewModel : ViewModel() {

  //this is used to store the state of the map and modify it
  var state by mutableStateOf(MapState())

  //Changes the map properties depending on the permission
  fun updatePermission(value: Boolean) {
    state.uiSettings = state.uiSettings.copy(myLocationButtonEnabled = value)
    state.properties = state.properties.copy(isMyLocationEnabled = value)
  }

  //Update the markers on the map with a new center location and radius
  fun updateMarkers(centerLocation: LatLng, rad: Double) {
    val lausanne =
      ClimbingMarker(
        "Lausanne",
        LatLng(46.519962, 6.633597),
        "Example climbing marker in lausanne",
        BitmapDescriptorFactory.fromResource(R.drawable.discover_icon))

    //call the api to retrieve the list of activities in a radius from the center location
    val repository = OutdoorActivityRepository()
    val activities = repository.getHikingActivities(rad.toInt(), centerLocation.latitude, centerLocation.longitude)
    println(rad)
    state.markerList = state.markerList.plus(lausanne)
  }
}
