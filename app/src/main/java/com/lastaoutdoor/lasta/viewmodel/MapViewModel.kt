package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.Node
import com.lastaoutdoor.lasta.data.model.Relation
import com.lastaoutdoor.lasta.data.model.map.ClimbingMarker
import com.lastaoutdoor.lasta.data.model.map.HikingMarker
import com.lastaoutdoor.lasta.data.model.map.MapItinerary
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository

class MapViewModel : ViewModel() {

  // this is used to store the state of the map and modify it
  var state by mutableStateOf(MapState())

  // if the user has not agreed to share his location, the map will be centered on Lausanne
  val initialPosition = LatLng(46.519962, 6.633597)

  // initial zoom level of the map
  val initialZoom = 11f

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

    // Converts the node data structure to our marker data structure
    climbingNodes.forEach {
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

  // Returns relations that represents hiking activities
  private fun fetchHikingActivities(
      rad: Double,
      centerLocation: LatLng,
      repository: OutdoorActivityRepository
  ): List<Relation> {

    var hikingRelation: List<Relation> = emptyList()

    val hikingThread = Thread {
      hikingRelation =
          repository
              .getHikingActivities(rad.toInt(), centerLocation.latitude, centerLocation.longitude)
              .elements
    }

    hikingThread.start()
    hikingThread.join()

    return hikingRelation
  }

  // Still WIP /!\ -> Get starting point of a hiking activity
  private fun getMarkersFromRelations(hikingRelations: List<Relation>): List<HikingMarker> {

    val markers = mutableListOf<HikingMarker>()

    hikingRelations.forEach {
      markers.add(
          HikingMarker(
              "Hiking: " + (it.tags.name ?: "Sans nom"),
              LatLng(it.bounds.minlat, it.bounds.minlon),
              it.locationName ?: "No description",
              BitmapDescriptorFactory.fromResource(R.drawable.hiking_icon)))
    }

    return markers.toList()
  }

  // Still WIP /!\ -> get the itinerary of a hiking activity (Doesn't work, we cannot just draw and
  // fetch all itineraries for big distances)
  private fun getItineraryFromRelations(hikingRelations: List<Relation>): List<MapItinerary> {

    val itinerary = mutableListOf<MapItinerary>()

    // go through each relation to find the ways and nodes composing it
    hikingRelations.forEach { relation ->
      val pointsList = mutableListOf<LatLng>()
      relation.ways.forEach { way ->
        way.nodes.forEach { position -> pointsList.add(LatLng(position.lat, position.lon)) }
      }

      itinerary.add(
          MapItinerary(
              relation.id, relation.tags.name ?: "Unnamed Hiking Spot", points = pointsList))
    }

    return itinerary.toList()
  }

  // Update the markers on the map with a new center location and radius
  fun updateMarkers(centerLocation: LatLng, rad: Double) {

    // fetch more activity at once, so less effort when moving around

    try {
      // repository to fetch the activities
      val repository = OutdoorActivityRepository()

      // get all the climbing activities in the radius
      val climbingMarkers = fetchClimbingActivities(rad, centerLocation, repository)

      // markers for hiking activities are still not ready due to optimization problems / api call
      // structure
      val hikingRelations = fetchHikingActivities(rad, centerLocation, repository)
      // val hikingMarkers = getMarkersFromRelations(hikingRelations)

      // Add the markers to the map
      state.markerList = climbingMarkers
      // state.markerList = state.markerList.plus(hikingMarkers)

      // state.itineraryList = state.itineraryList.plus(getItineraryFromRelations(hikingRelations))
    } catch (e: Exception) {
      // TODO: error message system to tell the user that the activities could not be fetched
      e.printStackTrace()
    }
  }
}
