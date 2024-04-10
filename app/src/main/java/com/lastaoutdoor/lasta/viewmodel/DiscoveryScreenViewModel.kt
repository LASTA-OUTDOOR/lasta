package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import com.lastaoutdoor.lasta.data.model.api.Node
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import com.lastaoutdoor.lasta.ui.screen.discovery.ActivityDialog
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
          OutdoorActivity(ActivityType.CLIMBING, node.difficulty, node.length, "", node.tags.name))
    }
  }
  val displayDialog =  mutableStateOf(false)
  @Composable
  fun displayActivityDialog(outdoorActivity: OutdoorActivity){
      if (displayDialog.value) {
          ActivityDialog(
              onDismissRequest = { displayDialog.value=false },
              outdoorActivity = outdoorActivity)
      }
  }

  fun showDialog(){
      displayDialog.value=true
  }
}
