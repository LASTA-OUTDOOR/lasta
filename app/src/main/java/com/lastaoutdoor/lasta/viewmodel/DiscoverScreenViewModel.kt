package com.lastaoutdoor.lasta.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.BikingActivity
import com.lastaoutdoor.lasta.models.activity.ClimbingActivity
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.HikingActivity
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.models.map.MapItinerary
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DiscoverScreenViewModel @Inject constructor(private val repository: ActivityRepository) :
    ViewModel() {

  val activities = mutableStateOf<ArrayList<Activity>>(ArrayList())

  private var _mapState = MutableStateFlow(MapState())
  val mapState: StateFlow<MapState> = _mapState

  private val _screen = MutableStateFlow(DiscoverDisplayType.LIST)
  val screen: StateFlow<DiscoverDisplayType> = _screen

  private val _range = MutableStateFlow(10000.0)
  val range: StateFlow<Double> = _range
  // if the user has not agreed to share his location, the map will be centered on Lausanne
  val initialPosition = LatLng(46.519962, 6.633597)

  // initial zoom level of the map
  val initialZoom = 11f

  // Zoom level when focusing on a marker
  val selectedZoom = 13f

  // Changes the map properties depending on the permission

  // List of markers to display on the map
  private val _markerList = MutableStateFlow<List<Marker>>(emptyList())
  val markerList: StateFlow<List<Marker>> = _markerList

  // Displayed itinerary
  private val _selectedItinerary = MutableStateFlow<MapItinerary?>(null)
  val selectedItinerary: StateFlow<MapItinerary?> = _selectedItinerary

  // The marker displayed in the more info bottom sheet
  private val _selectedMarker = MutableStateFlow<Marker?>(null)
  val selectedMarker: StateFlow<Marker?> = _selectedMarker

  // List of localities with their LatLng coordinates
  val localities =
      listOf(
          "Ecublens" to LatLng(46.519962, 6.633597),
          "Geneva" to LatLng(46.2043907, 6.1431577),
          "Payerne" to LatLng(46.834190, 6.928969),
          "Matterhorn" to LatLng(45.980537, 7.641618))
  // Selected locality
  private val _selectedLocality = MutableStateFlow(localities[0])
  val selectedLocality: StateFlow<Pair<String, LatLng>> = _selectedLocality

  init {
    activities.value.add(
        Activity(
            "1",
            11033919,
            ActivityType.HIKING,
            "Chemin panorama alpin",
            Position(46.4718332, 6.8338907),
            0.0f,
            0,
            emptyList(),
            difficulty = Difficulty.EASY,
            activityImageUrl = ""))

    activities.value.add(
        Activity(
            "2",
            11061599,
            ActivityType.HIKING,
            "Camino de Santiago",
            Position(46.5227881, 6.6359885),
            0.0f,
            0,
            emptyList(),
            difficulty = Difficulty.EASY,
            activityImageUrl = ""))

    _markerList.value = activitiesToMarkers(activities.value)
  }

  private fun activitiesToMarkers(activities: List<Activity>): List<Marker> {
    return activities.map { activity ->
      Marker(
          activity.osmId,
          activity.name,
          LatLng(activity.startPosition.lat, activity.startPosition.lon),
          "",
          R.drawable.hiking_icon,
          ActivityType.HIKING)
    }
  }

  fun fetchClimbingActivities(
      rad: Double = 10000.0,
      centerLocation: LatLng = LatLng(46.519962, 6.633597)
  ) {
    viewModelScope.launch {
      val response =
          repository.getClimbingPointsInfo(
              rad.toInt(), centerLocation.latitude, centerLocation.longitude)
      val climbingPoints = (response as Response.Success).data ?: emptyList()
      activities.value = ArrayList()
      climbingPoints.forEach { point ->
        activities.value.add(ClimbingActivity("", point.id, point.tags.name))
      }
    }
  }

  fun fetchHikingActivities(
      rad: Double = 10000.0,
      centerLocation: LatLng = LatLng(46.519962, 6.633597)
  ) {
    viewModelScope.launch {
      val response =
          repository.getHikingRoutesInfo(
              rad.toInt(), centerLocation.latitude, centerLocation.longitude)
      val hikingPoints = (response as Response.Success).data ?: emptyList()
      activities.value = ArrayList()
      hikingPoints.forEach { point ->
        activities.value.add(
            HikingActivity(
                "", point.id, point.tags.name, from = point.tags.from, to = point.tags.to))
      }
    }
  }

  fun fetchBikingActivities(
      rad: Double = 10000.0,
      centerLocation: LatLng = LatLng(46.519962, 6.633597)
  ) {
    viewModelScope.launch {
      val response =
          repository.getBikingRoutesInfo(
              rad.toInt(), centerLocation.latitude, centerLocation.longitude)
      val hikingPoints = (response as Response.Success).data ?: emptyList()
      activities.value = ArrayList()
      hikingPoints.forEach { point ->
        activities.value.add(
            BikingActivity(
                "",
                point.id,
                point.tags.name,
                from = point.tags.from,
                to = point.tags.to,
                distance = point.tags.distance.toFloat()))
      }
    }
  }

  fun setScreen(screen: DiscoverDisplayType) {
    _screen.value = screen
  }

  // Set the range for the activities
  fun setRange(range: Double) {
    _range.value = range
  }

  // Set the selected locality
  fun setSelectedLocality(locality: Pair<String, LatLng>) {
    _selectedLocality.value = locality
  }

  fun updatePermission(value: Boolean) {
    _mapState.value.uiSettings = _mapState.value.uiSettings.copy(myLocationButtonEnabled = value)
    _mapState.value.properties = _mapState.value.properties.copy(isMyLocationEnabled = value)
  }

  // Update which marker is currently selected
  fun updateSelectedMarker(marker: Marker) {
    _selectedMarker.value = marker
    showHikingItinerary(marker.id)
  }

  // Clear the selected itinerary
  fun clearSelectedItinerary() {
    _selectedItinerary.value = null
  }

  fun clearSelectedMarker() {
    _selectedMarker.value = null
  }

  private fun showHikingItinerary(id: Long) {
    viewModelScope.launch {
      val response = repository.getHikingRouteById(id)
      val itinerary = (response as Response.Success).data
      val pointsList = mutableListOf<LatLng>()
      itinerary?.ways?.forEach { way ->
        val nodes = way.nodes ?: emptyList()
        nodes.forEach { position -> pointsList.add(LatLng(position.lat, position.lon)) }
      }
      _selectedItinerary.value =
          MapItinerary(
              id,
              itinerary?.tags?.name ?: "",
              pointsList,
          )
    }
  }

  fun updateMarkers(centerLocation: LatLng, rad: Double) {

    // get all the climbing activities in the radius
    // fetchClimbingActivities(rad, centerLocation, activityRepository)

    // get all the hiking activities in the radius (only displays first point of the itinerary)
    // val hikingRelations = fetchHikingActivities(rad, centerLocation)
    // getMarkersFromRelations(hikingRelations)

    // Add the itineraries to a map -> can access them by relation id
    // getItineraryFromRelations(hikingRelations)
  }
}

enum class DiscoverDisplayType {
  LIST,
  MAP;

  fun toStringCon(con: Context): String {
    return when (this) {
      LIST -> con.getString(R.string.list)
      MAP -> con.getString(R.string.map)
    }
  }
}
