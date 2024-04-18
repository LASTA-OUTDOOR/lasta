package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
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

  var climbingActivities: MutableList<OutdoorActivity> = mutableListOf()

  private val _screen = MutableStateFlow(DiscoveryScreenType.LIST)
  val screen: StateFlow<DiscoveryScreenType> = _screen

  init {
    fetchClimbingActivities()
  }

  private fun fetchClimbingActivities(
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
          OutdoorActivity(
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
}
