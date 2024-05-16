package com.lastaoutdoor.lasta.viewmodel

import android.content.Context
import android.util.Log
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

// Initial values
const val INITIAL_RANGE = 10000.0

// All the function of the viewmodel that the view can call
data class DiscoverScreenCallBacks(
    val fetchActivities: () -> Unit,
    val setScreen: (DiscoverDisplayType) -> Unit,
    val setRange: (Double) -> Unit,
    val setSelectedLocality: (Pair<String, LatLng>) -> Unit,
    val updatePermission: (Boolean) -> Unit,
    val updateMarkers: (LatLng, Double) -> Unit,
    val updateSelectedMarker: (Marker?) -> Unit,
    val clearSelectedItinerary: () -> Unit,
    val updateOrderingBy: (OrderingBy) -> Unit,
    val clearSelectedMarker: () -> Unit,
    val fetchSuggestion: (String) -> Unit,
    val clearSuggestions: () -> Unit,
    val updateInitialPosition: (LatLng) -> Unit,
    val updateActivities: (List<Activity>) -> Unit,
    val updateRange: (Double) -> Unit,
    val setSelectedLevels: (UserActivitiesLevel) -> Unit,
    val setSelectedActivitiesType: (List<ActivityType>) -> Unit,
    val setShowCompleted: (Boolean) -> Unit
)

// Data class to store all the state of the viewmodel
data class DiscoverScreenState(
    val isLoading: Boolean = true,
    val activities: List<Activity> = emptyList(),
    val activityIds: List<Long> = emptyList(),
    val screen: DiscoverDisplayType = DiscoverDisplayType.LIST,
    val range: Double = INITIAL_RANGE,
    val localities: List<Pair<String, LatLng>> =
        listOf(
            "Ecublens" to LatLng(46.519962, 6.633597),
            "Geneva" to LatLng(46.2043907, 6.1431577),
            "Payerne" to LatLng(46.834190, 6.928969),
            "Matterhorn" to LatLng(45.980537, 7.641618)),
    val centerPoint: LatLng =
        localities[0].second, // default center point is Ecublens (to fetch activities from)
    val selectedLocality: Pair<String, LatLng> = localities[0],
    val mapState: MapState = MapState(),
    val initialPosition: LatLng = localities[0].second,
    val initialZoom: Float = 11f,
    val selectedZoom: Float = 13f, // Zoom level when focusing on a marker
    val selectedMarker: Marker? = null, // no marker is initially selected
    val selectedItinerary: MapItinerary? = null, // no itinerary is initially selected
    val markerList: List<Marker> = emptyList(),
    val orderingBy: OrderingBy = OrderingBy.DISTANCE,
    val suggestions: Map<String, LatLng> = emptyMap(),
    val selectedActivityTypes: List<ActivityType> = listOf(ActivityType.HIKING),
    val selectedLevels: UserActivitiesLevel =
        UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER),
    val showCompleted: Boolean = false
)

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

  // data class to store all the state of the viewModel, this allows to group them together and make
  // it easier to test the navgraph
  private val _state = MutableStateFlow(DiscoverScreenState())
  val state: StateFlow<DiscoverScreenState> = _state

  // Callbacks that the view can call
  val callbacks =
      DiscoverScreenCallBacks(
          fetchActivities = ::fetchActivities,
          setScreen = { screen -> setScreen(screen) },
          setRange = { range -> setRange(range) },
          setSelectedLocality = { locality -> setSelectedLocality(locality) },
          updatePermission = { value -> updatePermission(value) },
          updateMarkers = { centerLocation, rad -> updateMarkers(centerLocation, rad) },
          updateSelectedMarker = { marker -> updateSelectedMarker(marker) },
          clearSelectedItinerary = { clearSelectedItinerary() },
          updateOrderingBy = { orderingBy -> updateOrderingBy(orderingBy) },
          clearSelectedMarker = { clearSelectedMarker() },
          fetchSuggestion = { query -> fetchSuggestions(query) },
          clearSuggestions = { clearSuggestions() },
          updateInitialPosition = { position -> updateInitialPosition(position) },
          updateActivities = { activities -> updateActivities(activities) },
          updateRange = { range -> updateRange(range) },
          setSelectedLevels = { levels -> setSelectedLevels(levels) },
          setSelectedActivitiesType = { activitiesType ->
            setSelectedActivitiesType(activitiesType)
          },
          setShowCompleted = { showCompleted -> setShowCompleted(showCompleted) }
      )

  init {
    viewModelScope.launch {
      _state.value =
          _state.value.copy(
              selectedActivityTypes =
                  preferencesRepository.userPreferencesFlow
                      .map { listOf(it.user.prefActivity) }
                      .first())
      _state.value =
          _state.value.copy(
              selectedLevels =
                  preferencesRepository.userPreferencesFlow.map { it.user.levels }.first())

      val userId = preferencesRepository.userPreferencesFlow.map { it.user.userId }.first()
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
          R.drawable.hiking_icon,
          ActivityType.HIKING)
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
      _state.value =
          _state.value.copy(
              suggestions = suggestions.associate { it.getSuggestion() to it.getPosition() })
    }
  }

  // Clears the list of suggestions
  fun clearSuggestions() {
    _state.value = _state.value.copy(suggestions = emptyMap())
  }

  fun fetchActivities() {
    viewModelScope.launch {
      _state.value = _state.value.copy(isLoading = true)

      val activitiesHolder: ArrayList<Activity> = ArrayList()
      val activitiesIdsHolder: ArrayList<Long> = ArrayList()
      val markerListHolder: ArrayList<Marker> = ArrayList()
      for (activityType in _state.value.selectedActivityTypes) {
        val response =
            when (activityType) {
              ActivityType.CLIMBING ->
                  repository.getClimbingPointsInfo(
                      _state.value.range.toInt(),
                      _state.value.initialPosition.latitude,
                      _state.value.initialPosition.longitude)
              ActivityType.HIKING ->
                  repository.getHikingRoutesInfo(
                      _state.value.range.toInt(),
                      _state.value.initialPosition.latitude,
                      _state.value.initialPosition.longitude)
              ActivityType.BIKING ->
                  repository.getBikingRoutesInfo(
                      _state.value.range.toInt(),
                      _state.value.initialPosition.latitude,
                      _state.value.initialPosition.longitude)
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
        activitiesHolder.addAll(activitiesDB.getActivitiesByOSMIds(activitiesIdsHolder, _state.value.showCompleted))
        markerListHolder.addAll(activitiesToMarkers(activitiesHolder))
        // remove duplicates
        markerListHolder.distinct()
      }
      _state.value =
          _state.value.copy(
              activities = activitiesHolder.distinct(),
              activityIds = activitiesIdsHolder.distinct(),
              markerList = markerListHolder)
      // order the activities by the selected ordering
      updateActivitiesByOrdering()
      _state.value =
          _state.value.copy(
              isLoading = false,
          )
    }
  }

  fun setScreen(screen: DiscoverDisplayType) {
    _state.value = _state.value.copy(screen = screen)
  }

  // Set the range for the activities
  fun setRange(range: Double) {
    _state.value = _state.value.copy(range = range)
  }

    // toggle wether we show completed activities or not
  fun setShowCompleted(showCompleted: Boolean) {
    _state.value = _state.value.copy(showCompleted = showCompleted)
    fetchActivities()
  }

  // Set the selected locality
  fun setSelectedLocality(locality: Pair<String, LatLng>) {
    _state.value = _state.value.copy(selectedLocality = locality)
    _state.value = _state.value.copy(centerPoint = locality.second)
  }

  fun setSelectedActivitiesType(activitiesType: List<ActivityType>) {
    _state.value = _state.value.copy(selectedActivityTypes = activitiesType)
    fetchActivities()
  }

  fun setSelectedLevels(levels: UserActivitiesLevel) {
    _state.value = _state.value.copy(selectedLevels = levels)
    fetchActivities()
  }

  fun updatePermission(value: Boolean) {
    val mapState = _state.value.mapState
    mapState.uiSettings = mapState.uiSettings.copy(myLocationButtonEnabled = value)
    mapState.properties = mapState.properties.copy(isMyLocationEnabled = value)
    _state.value = _state.value.copy(mapState = mapState)
  }

  // Update which marker is currently selected
  fun updateSelectedMarker(marker: Marker?) {
    if (marker == null) { // if the marker is null, clear the selected itinerary
      clearSelectedItinerary()
      return
    }
    _state.value = _state.value.copy(selectedMarker = marker)
    showHikingItinerary(marker.id)
  }

  // Clear the selected itinerary
  fun clearSelectedItinerary() {
    _state.value = _state.value.copy(selectedItinerary = null)
  }

  // Clear the selected marker
  fun clearSelectedMarker() {
    _state.value = _state.value.copy(selectedMarker = null)
  }

  // change the default place on the map
  fun updateInitialPosition(position: LatLng) {
    _state.value = _state.value.copy(initialPosition = position)
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

      _state.value =
          _state.value.copy(
              selectedItinerary = MapItinerary(id, itinerary?.tags?.name ?: "", pointsList))
    }
  }

  fun updateActivityType(activitiesType: List<ActivityType>) {
    _state.value = _state.value.copy(selectedActivityTypes = activitiesType)
    fetchActivities()
  }

  // function used only for testing purposes
  fun updateActivities(activities: List<Activity>) {
    _state.value = _state.value.copy(activities = activities)
  }

  private fun updateActivitiesByOrdering() {
    if (_state.value.activities.isEmpty()) return
    when (_state.value.orderingBy) {
      OrderingBy.DISTANCE -> {
        val distances =
            _state.value.activities.map {
              SphericalUtil.computeDistanceBetween(
                  _state.value.selectedLocality.second,
                  LatLng(it.startPosition.lat, it.startPosition.lon))
            }
        val sortedActivities =
            _state.value.activities.sortedBy { distances[_state.value.activities.indexOf(it)] }
        _state.value = _state.value.copy(activities = sortedActivities)
      }
      OrderingBy.RATING -> {
        _state.value =
            _state.value.copy(
                activities = (_state.value.activities.sortedBy { it.rating }.reversed()))
      }
      OrderingBy.POPULARITY -> {
        _state.value =
            _state.value.copy(
                activities = (_state.value.activities.sortedBy { it.numRatings }.reversed()))
      }
      OrderingBy.DIFFICULTYASCENDING -> {
        _state.value =
            _state.value.copy(activities = (_state.value.activities.sortedBy { it.difficulty }))
      }
      OrderingBy.DIFFICULTYDESCENDING -> {
        _state.value =
            _state.value.copy(
                activities = (_state.value.activities.sortedBy { it.difficulty }.reversed()))
      }
    }
    _state.value =
        _state.value.copy(
            activities =
                _state.value.activities.filter { filterWithDiff(_state.value.selectedLevels, it) })
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
    _state.value = _state.value.copy(orderingBy = orderingBy)
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
    _state.value = _state.value.copy(range = range)
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
