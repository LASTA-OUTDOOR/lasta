package com.lastaoutdoor.lasta.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository
import com.lastaoutdoor.lasta.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DiscoverScreenViewModel
@Inject
constructor(
    private val repository: ActivityRepository,
    private val activitiesDB: ActivitiesDBRepository
) : ViewModel() {

  val activities = mutableStateOf<ArrayList<Activity>>(ArrayList())
  val activityIds = mutableStateOf<ArrayList<Long>>(ArrayList())

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
    fetchClimbingActivities()
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
      activityIds.value = ArrayList()
      climbingPoints.map { point ->
        activityIds.value.add(point.id)
        //activitiesDB.addActivity(Activity("", point.id, ActivityType.CLIMBING, point.tags.name))
      }
      activities.value = activitiesDB.getActivitiesByOSMIds(activityIds.value) as ArrayList<Activity>
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
            Activity(
                "",
                point.id,
                ActivityType.HIKING,
                point.tags.name,
                from = point.tags.from,
                to = point.tags.to))
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
