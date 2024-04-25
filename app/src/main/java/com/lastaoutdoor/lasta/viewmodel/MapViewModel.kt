package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.api.NodeWay
import com.lastaoutdoor.lasta.data.model.map.ClimbingMarker
import com.lastaoutdoor.lasta.data.model.map.Marker
import com.lastaoutdoor.lasta.repository.ActivityRepository
import com.lastaoutdoor.lasta.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MapViewModel @Inject constructor(private val activityRepository: ActivityRepository) :
    ViewModel() {

  // this is used to store the state of the map and modify it
  var state by mutableStateOf(MapState())

  // if the user has not agreed to share his location, the map will be centered on Lausanne
  val initialPosition = LatLng(46.519962, 6.633597)

  // initial zoom level of the map
  val initialZoom = 11f

  // Zoom level when focusing on a marker
  val selectedZoom = 13f

  // Changes the map properties depending on the permission
  fun updatePermission(value: Boolean) {
    state.uiSettings = state.uiSettings.copy(myLocationButtonEnabled = value)
    state.properties = state.properties.copy(isMyLocationEnabled = value)
  }

  // Update which marker is currently selected
  fun updateSelectedMarker(marker: Marker) {
    state.selectedMarker = marker
  }

  // Update the selected itinerary which will draw it on the map
  fun updateSelectedItinerary(relId: Long) {
    state.selectedItinerary = state.itineraryMap[relId]
  }

  // Clear the selected itinerary
  fun clearSelectedItinerary() {
    state.selectedItinerary = null
  }

  // Calls the API to fetch climbing activities and returns the list of markers to display
  private fun fetchClimbingActivities(
      rad: Double,
      centerLocation: LatLng,
      repository: ActivityRepository
  ) {

    viewModelScope.launch {
      val response =
          repository.getClimbingPointsInfo(
              rad.toInt(), centerLocation.latitude, centerLocation.longitude)
      val climbingPoints: List<NodeWay> = (response as Response.Success).data ?: emptyList()

      val climbingMarkers = mutableListOf<ClimbingMarker>()

      // Converts the node data structure to our marker data structure
      climbingPoints.forEach {
        val marker =
            ClimbingMarker(
                it.tags.name,
                LatLng(it.lat ?: 46.55, it.lon ?: 6.549),
                it.tags.sport ?: "climbing",
                R.drawable.climbing_icon)
        climbingMarkers.add(marker)
      }

      state.markerList = climbingMarkers.toList()
    }
  }

  // Returns relations that represents hiking activities
  /*private fun fetchHikingActivities(rad: Double, centerLocation: LatLng): List<Relation> {

    var hikingRelation: List<Relation> = emptyList()

    val hikingThread = Thread {
      hikingRelation =
          activityRepository
              .getHikingActivities(rad.toInt(), centerLocation.latitude, centerLocation.longitude)
              .elements
    }

    hikingThread.start()
    hikingThread.join()

    return hikingRelation
  }

  // Converts the relations to markers -> starting marker of a hike (takes first point)
  private fun getMarkersFromRelations(hikingRelations: List<Relation>) {

    val markers = mutableListOf<HikingMarker>()

    // store the id of the relation in a map of the view model
    hikingRelations.forEach {
      val rel = it.ways ?: emptyList()
      if (rel.isNotEmpty()) {
        val node = rel.first().nodes ?: emptyList()
        if (node.isNotEmpty()) {

          val hikingMarker =
              HikingMarker(
                  "Hiking: " + (it.tags.name ?: "Unnamed"),
                  LatLng(node.first().lat, node.first().lon),
                  it.locationName ?: "No location name",
                  R.drawable.hiking_icon,
                  it.id)

          markers.add(hikingMarker)
        }
      }
    }

    // write directly in the state
    state.itineraryStartMarkers = markers.toList()
  }

  // Puts a mapping from relation Id to itinerary in a map of the view model (side effect, no
  // return)
  private fun getItineraryFromRelations(hikingRelations: List<Relation>) {

    // go through each relation to find the ways and nodes composing it
    hikingRelations.forEach { relation ->
      val relId = relation.id
      val ways = relation.ways ?: emptyList()

      val pointsList = mutableListOf<LatLng>()
      ways.forEach { way ->
        val nodes = way.nodes ?: emptyList()
        nodes.forEach { position -> pointsList.add(LatLng(position.lat, position.lon)) }
      }

      state.itineraryMap =
          state.itineraryMap.plus(
              relId to
                  MapItinerary(relId, relation.tags.name ?: "No given name", points = pointsList))
    }
  }*/

  // Update the markers on the map with a new center location and radius
  fun updateMarkers(centerLocation: LatLng, rad: Double) {

    // get all the climbing activities in the radius
    fetchClimbingActivities(rad, centerLocation, activityRepository)

    // get all the hiking activities in the radius (only displays first point of the itinerary)
    // val hikingRelations = fetchHikingActivities(rad, centerLocation)
    // getMarkersFromRelations(hikingRelations)

    // Add the itineraries to a map -> can access them by relation id
    // getItineraryFromRelations(hikingRelations)
  }
}
