package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.data.model.activity.Activity
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.Difficulty
import com.lastaoutdoor.lasta.data.model.api.NodeWay
import com.lastaoutdoor.lasta.repository.ActivityRepository
import com.lastaoutdoor.lasta.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class DiscoveryScreenType {
  LIST,
  MAP;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }
}

@HiltViewModel
class DiscoveryScreenViewModel @Inject constructor(private val repository: ActivityRepository) :
    ViewModel() {

  var climbingActivities: MutableList<Activity> = mutableListOf()

  private val _screen = MutableStateFlow(DiscoveryScreenType.LIST)
  val screen: StateFlow<DiscoveryScreenType> = _screen

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
      val response = repository.getClimbingPointsInfo(rad.toInt(), centerLocation.latitude, centerLocation.longitude)
      val climbingPoints = (response as Response.Success).data ?: emptyList()
      println(climbingPoints)
      climbingActivities.clear()
      climbingPoints.forEach { point ->
        climbingActivities.add(Activity(ActivityType.CLIMBING, Difficulty.EASY, 0f, "", point.tags.name))
      }
    }
  }

  fun setScreen(screen: DiscoveryScreenType) {
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
