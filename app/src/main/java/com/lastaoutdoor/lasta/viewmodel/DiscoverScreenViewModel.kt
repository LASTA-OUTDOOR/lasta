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
import com.lastaoutdoor.lasta.models.activity.HikingActivity
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
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
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

  val activities = mutableStateOf<ArrayList<Activity>>(ArrayList())

  private val _selectedActivityType = MutableStateFlow(ActivityType.CLIMBING)
  val selectedActivityType: StateFlow<ActivityType> = _selectedActivityType

  private val _selectedLevels = MutableStateFlow(UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER))
  val selectedLevels: StateFlow<UserActivitiesLevel> = _selectedLevels

  private val _screen = MutableStateFlow(DiscoverDisplayType.LIST)
  val screen: StateFlow<DiscoverDisplayType> = _screen

  private val _range = MutableStateFlow(10000.0)
  val range: StateFlow<Double> = _range

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

        _selectedLevels.value = preferencesRepository.userPreferencesFlow.map { it.user.levels }.first()
      fetchClimbingActivities()
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

  fun setSelectedActivityType(activityType: ActivityType) {
    _selectedActivityType.value = activityType
    when (activityType) {
      ActivityType.CLIMBING -> fetchClimbingActivities(range.value, selectedLocality.value.second)
      ActivityType.HIKING -> fetchHikingActivities(range.value, selectedLocality.value.second)
      ActivityType.BIKING -> fetchBikingActivities(range.value, selectedLocality.value.second)
    }
  }

  fun setSelectedLevels(levels: UserActivitiesLevel) {
    _selectedLevels.value = levels
  }
}

enum class DiscoverDisplayType {
  LIST,
  MAP;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }

  fun toStringCon(con: Context): String {
    return when (this) {
      LIST -> con.getString(R.string.list)
      MAP -> con.getString(R.string.map)
    }
  }

  fun toStringConDisp(con: Context): (DiscoverDisplayType) -> String {
    return { it -> it.toStringCon(con) }
  }
}
