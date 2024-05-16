package com.lastaoutdoor.lasta.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.messaging.FirebaseMessaging
import com.google.maps.android.SphericalUtil
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.api.NodeWay
import com.lastaoutdoor.lasta.models.api.OSMData
import com.lastaoutdoor.lasta.models.api.RadarSuggestion
import com.lastaoutdoor.lasta.models.api.Relation
import com.lastaoutdoor.lasta.models.map.MapItinerary
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.repository.api.RadarRepository
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository
import com.lastaoutdoor.lasta.repository.db.TokenDBRepository
import com.lastaoutdoor.lasta.utils.OrderingBy
import com.lastaoutdoor.lasta.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class DiscoverScreenViewModel
@Inject
constructor(
    private val repository: ActivityRepository,
    private val preferencesRepository: PreferencesRepository,
    private val activitiesDB: ActivitiesDBRepository,
    private val radarRepository: RadarRepository,
    private val tokenDBRepository: TokenDBRepository
) : ViewModel() {

  private val _isLoading = MutableStateFlow(true)
  val isLoading: StateFlow<Boolean> = _isLoading

  private val _activities = MutableStateFlow<List<Activity>>(emptyList())
  val activities: StateFlow<List<Activity>> = _activities

  private val _activityIds = MutableStateFlow<List<Long>>(emptyList())
  val activityIds: StateFlow<List<Long>> = _activityIds

  private val _orderingBy = MutableStateFlow(OrderingBy.DISTANCE)
  val orderingBy: StateFlow<OrderingBy> = _orderingBy

  private val _selectedActivityTypes = MutableStateFlow(listOf(ActivityType.HIKING))
  val selectedActivityType: StateFlow<List<ActivityType>> = _selectedActivityTypes

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

  // Position of the map when it is first displayed
  private val _initialPosition = MutableStateFlow(LatLng(46.519962, 6.633597))
  val initialPosition: StateFlow<LatLng> = _initialPosition

  // initial zoom level of the map
  val initialZoom = 11f

  // Zoom level when focusing on a marker
  val selectedZoom = 16f

  // Changes the map properties depending on the permission

  // List of markers to display on the map
  private val _markerList = MutableStateFlow<List<Marker>>(emptyList())
  val markerList: StateFlow<List<Marker>> = _markerList

  // Map of suggestions from the radar API with the locality as key and the LatLng as value
  private val _suggestions = MutableStateFlow<Map<String, LatLng>>(emptyMap())
  val suggestions: StateFlow<Map<String, LatLng>> = _suggestions

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
      val userId = preferencesRepository.userPreferencesFlow.map { it.user.userId }.first()
      _selectedActivityTypes.value =
          preferencesRepository.userPreferencesFlow.map { listOf(it.user.prefActivity) }.first()

      _selectedLevels.value =
          preferencesRepository.userPreferencesFlow.map { it.user.levels }.first()
      val token = FirebaseMessaging.getInstance().token.await()
      tokenDBRepository.uploadUserToken(userId, token)

      fetchActivities()
    }
  }

  fun activitiesToMarkers(activities: List<Activity>): List<Marker> {
    return activities.map { activity ->
      Marker(
          activity.osmId,
          activity.name,
          LatLng(activity.startPosition.lat, activity.startPosition.lon),
          "",
          when (activity.activityType) {
            ActivityType.CLIMBING -> R.drawable.climbing_icon
            ActivityType.HIKING -> R.drawable.hiking_icon
            ActivityType.BIKING -> R.drawable.biking_icon
          },
          activity.activityType)
    }
  }

  // Fetch the suggestions from the radar API (called when the user types in the search bar)
  fun fetchSuggestions(query: String) {
    viewModelScope.launch {
      val suggestions =
          when (val response = radarRepository.getSuggestions(query)) {
            is Response.Failure -> {
              response.e.printStackTrace()
              return@launch
            }
            is Response.Success -> {
              response.data ?: emptyList()
            }
            is Response.Loading -> {
              emptyList<RadarSuggestion>()
            }
          }
      _suggestions.value = suggestions.map { it.getSuggestion() to it.getPosition() }.toMap()
    }
  }

  // Clears the list of suggestions
  fun clearSuggestions() {
    _suggestions.value = emptyMap()
  }

  fun fetchActivities() {
    viewModelScope.launch {
      _isLoading.value = true
      val activitiesHolder: ArrayList<Activity> = ArrayList()
      val activitiesIdsHolder: ArrayList<Long> = ArrayList()
      val markerListHolder: ArrayList<Marker> = ArrayList()
      for (activityType in _selectedActivityTypes.value) {
        val response =
            when (activityType) {
              ActivityType.CLIMBING ->
                  repository.getClimbingPointsInfo(
                      _range.value.toInt(),
                      _initialPosition.value.latitude,
                      _initialPosition.value.longitude)
              ActivityType.HIKING ->
                  repository.getHikingRoutesInfo(
                      _range.value.toInt(),
                      _initialPosition.value.latitude,
                      _initialPosition.value.longitude)
              ActivityType.BIKING ->
                  repository.getBikingRoutesInfo(
                      _range.value.toInt(),
                      _initialPosition.value.latitude,
                      _initialPosition.value.longitude)
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

        osmData.map { point ->
          when (activityType) {
            ActivityType.CLIMBING -> {
              val castedPoint = point as NodeWay
              activitiesIdsHolder.add(castedPoint.id)
              activitiesDB.addActivityIfNonExisting(
                  Activity("", point.id, ActivityType.CLIMBING, point.tags.name))
            }
            ActivityType.HIKING -> {
              val castedPoint = point as Relation
              activitiesIdsHolder.add(castedPoint.id)
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
              activitiesIdsHolder.add(castedPoint.id)
              val distance =
                  if (point.tags.distance.isEmpty()) 0f else point.tags.distance.toFloat()
              activitiesDB.addActivityIfNonExisting(
                  Activity(
                      "",
                      point.id,
                      ActivityType.BIKING,
                      point.tags.name,
                      from = point.tags.from,
                      to = point.tags.to,
                      distance = distance))
            }
          }
        }
        activitiesHolder.addAll(activitiesDB.getActivitiesByOSMIds(activitiesIdsHolder, false))
        markerListHolder.addAll(activitiesToMarkers(activitiesHolder))
      }

      _activities.value = activitiesHolder
      _activityIds.value = activitiesIdsHolder
      _markerList.value = markerListHolder
      // order the activities by the selected ordering
      updateActivitiesByOrdering()
      _isLoading.value = false
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
    _initialPosition.value = locality.second
  }

  fun setSelectedActivitiesType(activitiesType: List<ActivityType>) {
    _selectedActivityTypes.value = activitiesType
    fetchActivities()
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
    // update the marker list
    val markerListHolder = ArrayList(_markerList.value)
    if (!markerList.value.contains(marker)) {
      markerListHolder.add(marker!!)
    }
    _markerList.value = markerListHolder

    // update the selected marker
    _selectedMarker.value = marker
    val id = marker?.id ?: 0L

    if (id == 0L) {
      _selectedItinerary.value = null
      return
    }

    val activityType =
        when (marker?.icon) {
          R.drawable.hiking_icon -> ActivityType.HIKING
          R.drawable.biking_icon -> ActivityType.BIKING
          R.drawable.climbing_icon -> ActivityType.CLIMBING
          else -> ActivityType.HIKING
        }

    // show itinerary
    showItinerary(id, marker?.position ?: LatLng(0.0, 0.0), activityType)
  }

  // Clear the selected itinerary
  fun clearSelectedItinerary() {
    _selectedItinerary.value = null
  }

  fun clearSelectedMarker() {
    _selectedMarker.value = null
  }

  // change the default place on the map
  fun updateInitialPosition(position: LatLng) {
    _initialPosition.value = position
  }

  private fun showItinerary(id: Long, startPosition: LatLng, activityType: ActivityType) {
    viewModelScope.launch {
      val response =
          when (activityType) {
            ActivityType.HIKING -> repository.getHikingRouteById(id)
            ActivityType.BIKING -> repository.getBikingRouteById(id)
            ActivityType.CLIMBING -> repository.getClimbingPointById(id)
          }
      val itinerary = (response as Response.Success).data

      when (activityType) {
        ActivityType.HIKING,
        ActivityType.BIKING -> showRouteItinerary(id, itinerary as Relation, startPosition)
        ActivityType.CLIMBING -> showClimbingItinerary(id, itinerary as NodeWay, startPosition)
      }
    }
  }

  private fun showRouteItinerary(id: Long, itinerary: Relation, startPosition: LatLng) {
    val pointsList = mutableListOf<LatLng>()
    var currentStartPosition = startPosition

    itinerary.ways?.forEach { way ->
      val nodes = way.nodes ?: emptyList()
      if (nodes.isNotEmpty()) {
        // Calculate distances from the current start position to the first and last node
        val firstNode = nodes[0]
        val lastNode = nodes[nodes.size - 1]

        val firstNodeDistanceFromStart =
            SphericalUtil.computeDistanceBetween(
                currentStartPosition, LatLng(firstNode.lat, firstNode.lon))

        val lastNodeDistanceFromStart =
            SphericalUtil.computeDistanceBetween(
                currentStartPosition, LatLng(lastNode.lat, lastNode.lon))

        // Determine if the nodes list needs to be reversed
        val orderedNodes =
            if (lastNodeDistanceFromStart < firstNodeDistanceFromStart) {
              nodes.reversed()
            } else {
              nodes
            }

        // Add the ordered nodes to the points list
        orderedNodes.forEach { position -> pointsList.add(LatLng(position.lat, position.lon)) }

        // Update the current start position to the last node in the ordered list
        currentStartPosition = pointsList.last()
      }
    }

    _selectedItinerary.value =
        MapItinerary(
            id,
            itinerary.tags.name,
            pointsList,
        )
  }

  private fun showClimbingItinerary(id: Long, itinerary: NodeWay, startPosition: LatLng) {
    return /* TODO */
  }

  fun updateActivityType(activitiesType: List<ActivityType>) {
    _selectedActivityTypes.value = activitiesType
    fetchActivities()
  }

  // function used only for testing purposes
  fun updateActivities(activities: List<Activity>) {
    _activities.value = activities
  }

  private fun updateActivitiesByOrdering() {
    if (_activities.value.isEmpty()) return
    when (_orderingBy.value) {
      OrderingBy.DISTANCE -> {
        val distances =
            _activities.value.map {
              SphericalUtil.computeDistanceBetween(
                  selectedLocality.value.second, LatLng(it.startPosition.lat, it.startPosition.lon))
            }
        val sortedActivities =
            _activities.value.sortedBy { distances[_activities.value.indexOf(it)] }
        _activities.value = sortedActivities
      }
      OrderingBy.RATING -> {
        _activities.value = _activities.value.sortedBy { it.rating }.reversed()
      }
      OrderingBy.POPULARITY -> {
        _activities.value = _activities.value.sortedBy { it.numRatings }.reversed()
      }
      OrderingBy.DIFFICULTYASCENDING -> {
        _activities.value = _activities.value.sortedBy { it.difficulty }
      }
      OrderingBy.DIFFICULTYDESCENDING -> {
        _activities.value = _activities.value.sortedBy { it.difficulty }.reversed()
      }
    }
    _activities.value = _activities.value.filter { filterWithDiff(_selectedLevels.value, it) }
  }

  fun filterWithDiff(difficulties: UserActivitiesLevel, activity: Activity): Boolean {
    return when (activity.activityType) {
      ActivityType.CLIMBING -> {
        activity.difficulty <= userLevelToDifficulty(difficulties.climbingLevel)
      }
      ActivityType.HIKING -> {
        activity.difficulty <= userLevelToDifficulty(difficulties.hikingLevel)
      }
      ActivityType.BIKING -> {
        activity.difficulty <= userLevelToDifficulty(difficulties.bikingLevel)
      }
    }
  }

  private fun userLevelToDifficulty(userLevel: UserLevel): Difficulty {
    return when (userLevel) {
      UserLevel.BEGINNER -> Difficulty.EASY
      UserLevel.INTERMEDIATE -> Difficulty.NORMAL
      UserLevel.ADVANCED -> Difficulty.HARD
    }
  }

  fun updateOrderingBy(orderingBy: OrderingBy) {
    _orderingBy.value = orderingBy
    updateActivitiesByOrdering()
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

  fun updateRange(range: Double) {
    _range.value = range
    fetchActivities()
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
