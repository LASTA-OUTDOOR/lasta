package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import com.lastaoutdoor.lasta.data.model.api.Node
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiscoveryScreenViewModel
@Inject
constructor(private val repository: OutdoorActivityRepository) : ViewModel() {

  var climbingActivities: ArrayList<OutdoorActivity> = ArrayList()

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
  /*Toggles the dialog box on/off*/
  val displayDialog = mutableStateOf(false)
  /*Just a default activity to fill in the mutable state*/
  val dummyActivity = OutdoorActivity(ActivityType.HIKING, 3, 5.0f, "2 hours", "Zurich")
  val activityToDisplay = mutableStateOf(dummyActivity)

  fun showDialog(outdoorActivity: OutdoorActivity) {
    activityToDisplay.value = outdoorActivity
    displayDialog.value = true
  }
}
