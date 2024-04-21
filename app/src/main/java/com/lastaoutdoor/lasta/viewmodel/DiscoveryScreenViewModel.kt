package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.data.model.activity.Activity
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.api.Node
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class DiscoveryScreenType {
  LIST,
  MAP;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }
}

@HiltViewModel
class DiscoveryScreenViewModel
@Inject
constructor(private val repository: OutdoorActivityRepository) : ViewModel() {

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
    climbingActivities.clear()
    climbingNodes.forEach { node ->
      climbingActivities.add(
          Activity(
              ActivityType.CLIMBING,
              node.difficulty,
              node.length,
              "",
              node.tags.name ?: "Unnamed Climbing Activity"))
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
