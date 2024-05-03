package com.lastaoutdoor.lasta.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.api.NodeWay
import com.lastaoutdoor.lasta.models.api.OSMData
import com.lastaoutdoor.lasta.models.api.Relation
import com.lastaoutdoor.lasta.models.map.MapItinerary
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository
import com.lastaoutdoor.lasta.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class DiscoverScreenViewModel
@Inject
constructor(
    private val repository: ActivityRepository,
    private val preferencesRepository: PreferencesRepository,
    private val activitiesDB: ActivitiesDBRepository
) : ViewModel() {

  val activities = mutableStateOf<ArrayList<Activity>>(ArrayList())
  val activityIds = mutableStateOf<ArrayList<Long>>(ArrayList())

  private val _selectedActivityType = MutableStateFlow(ActivityType.CLIMBING)
  val selectedActivityType: StateFlow<ActivityType> = _selectedActivityType

  private val _selectedLevels =
      MutableStateFlow(
          UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER))
  val selectedLevels: StateFlow<UserActivitiesLevel> = _selectedLevels

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
    viewModelScope.launch {
      _selectedActivityType.value =
          preferencesRepository.userPreferencesFlow.map { it.user.prefActivity }.first()

      _selectedLevels.value =
          preferencesRepository.userPreferencesFlow.map { it.user.levels }.first()
      fetchActivities()
    }
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

  fun fetchActivities(rad: Double = 10000.0, centerLocation: LatLng = LatLng(46.519962, 6.633597)) {
    viewModelScope.launch {
      val response =
          when (_selectedActivityType.value) {
            ActivityType.CLIMBING ->
                repository.getClimbingPointsInfo(
                    rad.toInt(), centerLocation.latitude, centerLocation.longitude)
            ActivityType.HIKING ->
                repository.getHikingRoutesInfo(
                    rad.toInt(), centerLocation.latitude, centerLocation.longitude)
            ActivityType.BIKING ->
                repository.getBikingRoutesInfo(
                    rad.toInt(), centerLocation.latitude, centerLocation.longitude)
          }
      val osmData =
          when (response) {
            is Response.Failure -> {
              response.e.printStackTrace()
              return@launch
            }
            is Response.Success -> {
              response.data ?: emptyList()
            }
            is Response.Loading -> {
              emptyList<OSMData>()
            }
          }
      activities.value = ArrayList()
      activityIds.value = ArrayList()
      osmData.map { point ->
        when (_selectedActivityType.value) {
          ActivityType.CLIMBING -> {
            val castedPoint = point as NodeWay
            activityIds.value.add(castedPoint.id)
            activitiesDB.addActivityIfNonExisting(
                Activity("", point.id, ActivityType.CLIMBING, point.tags.name))
          }
          ActivityType.HIKING -> {
            val castedPoint = point as Relation
            activityIds.value.add(castedPoint.id)
            activitiesDB.addActivityIfNonExisting(
                Activity(
                    "",
                    point.id,
                    ActivityType.HIKING,
                    point.tags.name,
                    from = point.tags.from,
                    to = point.tags.to))
          }
          ActivityType.BIKING -> {
            val castedPoint = point as Relation
            activityIds.value.add(castedPoint.id)
            activitiesDB.addActivityIfNonExisting(
                Activity(
                    "",
                    point.id,
                    ActivityType.BIKING,
                    point.tags.name,
                    from = point.tags.from,
                    to = point.tags.to,
                    distance = point.tags.distance.toFloat()))
          }
        }
      }
      activities.value =
          activitiesDB.getActivitiesByOSMIds(activityIds.value, true) as ArrayList<Activity>
      _markerList.value = activitiesToMarkers(activities.value)
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

  fun setSelectedActivityType(activityType: ActivityType) {
    _selectedActivityType.value = activityType
  }

  fun setSelectedLevels(levels: UserActivitiesLevel) {
    _selectedLevels.value = levels
  }

  fun updatePermission(value: Boolean) {
    _mapState.value.uiSettings = _mapState.value.uiSettings.copy(myLocationButtonEnabled = value)
    _mapState.value.properties = _mapState.value.properties.copy(isMyLocationEnabled = value)
  }

  // Update which marker is currently selected
  fun updateSelectedMarker(marker: Marker?) {
    _selectedMarker.value = marker
    showHikingItinerary(marker?.id ?: 0L)
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

  fun updateActivityType(activityType: ActivityType) {
    _selectedActivityType.value = activityType
    fetchActivities()
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
