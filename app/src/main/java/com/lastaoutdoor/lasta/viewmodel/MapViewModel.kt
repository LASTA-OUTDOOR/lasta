package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.db.ClimbingMarker
import com.lastaoutdoor.lasta.data.model.Node
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository

class MapViewModel : ViewModel() {

  // this is used to store the state of the map and modify it
  var state by mutableStateOf(MapState())

  // if the user has not agreed to share his location, the map will be centered on Lausanne
  val initialPosition = LatLng(46.519962, 6.633597)

  // Changes the map properties depending on the permission
  fun updatePermission(value: Boolean) {
    state.uiSettings = state.uiSettings.copy(myLocationButtonEnabled = value)
    state.properties = state.properties.copy(isMyLocationEnabled = value)
  }

  // Calls the API to fetch climbing activities and returns the list of markers to display
  private fun fetchClimbingActivities(
      rad: Double,
      centerLocation: LatLng,
      repository: OutdoorActivityRepository
  ): List<ClimbingMarker> {

    // list that will store the climbing activities nodes
    var climbingNodes: List<Node> = emptyList()

    // Since the API call is a network call, it needs to be done in a separate thread
    val climbingThread = Thread {
      climbingNodes =
          repository
              .getClimbingActivitiesNode(
                  rad.toInt(), // in meters
                  centerLocation.latitude,
                  centerLocation.longitude)
              .elements
    }

    // start and join the thread, since we need the result before continuing
    climbingThread.start()
    climbingThread.join()

    val climbingMarkers = mutableListOf<ClimbingMarker>()

    climbingNodes.forEach() {
      val marker =
          ClimbingMarker(
              it.tags.name ?: "Unnamed Climbing Spot",
              LatLng(it.lat, it.lon),
              it.tags.sport,
              BitmapDescriptorFactory.fromResource(R.drawable.climbing_icon))
      climbingMarkers.add(marker)
    }

    return climbingMarkers
  }

  // Update the markers on the map with a new center location and radius
  fun updateMarkers(centerLocation: LatLng, rad: Double) {

    try {
      // repository to fetch the activities
      val repository = OutdoorActivityRepository()

      // get all the climbing activities in the radius
      val climbingMarkers = fetchClimbingActivities(rad, centerLocation, repository)

      // clear all the old markers
      state.markerList = state.markerList.dropLast(state.markerList.size)

      // TODO: Get all the hiking activities in the radius
      state.markerList = state.markerList.plus(climbingMarkers)
    } catch (e: Exception) {
      // TODO: error message system to tell the user that the activities could not be fetched
      e.printStackTrace()
    }
  }
}
