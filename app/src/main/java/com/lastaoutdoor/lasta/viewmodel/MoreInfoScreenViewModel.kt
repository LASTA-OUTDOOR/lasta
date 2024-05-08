package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository
import com.lastaoutdoor.lasta.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MoreInfoScreenViewModel
@Inject
constructor(
    private val activityRepository: ActivityRepository,
    private val activityDB: ActivitiesDBRepository
) : ViewModel() {
  /* Just a default activity to fill in the mutable state*/
  private val dummyActivity = Activity("", 0, ActivityType.CLIMBING, "Dummy")
  val activityToDisplay = mutableStateOf(dummyActivity)

  private val _isMapDisplayed = MutableStateFlow(false)
  val isMapDisplayed = _isMapDisplayed
  /*Changes the int difficulty of the activity for its String equivalent : 0 -> Easy, 1 -> Medium, etc...*/
  fun processDiffText(activity: Activity): String {
    return when (activity.difficulty) {
      Difficulty.EASY -> "Easy"
      Difficulty.NORMAL -> "Normal"
      Difficulty.HARD -> "Hard"
    }
  }

  fun changeActivityToDisplay(activity: Activity) {
    viewModelScope.launch {
      when (val response =
          when (activity.activityType) {
            ActivityType.CLIMBING -> activityRepository.getClimbingPointById(activity.osmId)
            ActivityType.HIKING -> activityRepository.getHikingRouteById(activity.osmId)
            ActivityType.BIKING -> activityRepository.getBikingRouteById(activity.osmId)
          }) {
        is Response.Loading -> {}
        is Response.Success -> {
          val osmData = response.data!!
          val updatedActivity = activity.copy(startPosition = osmData.getPosition())
          activityDB.updateStartPosition(activity.activityId, osmData.getPosition())
          activityToDisplay.value = updatedActivity
        }
        is Response.Failure -> {
          /*TODO*/
        }
      }
    }
  }

  fun goToMarker(activity: Activity): Marker {
    val icon =
        when (activity.activityType) {
          ActivityType.CLIMBING -> R.drawable.climbing_icon
          ActivityType.HIKING -> R.drawable.hiking_icon
          ActivityType.BIKING -> R.drawable.hiking_icon
        }
    return Marker(
        activity.osmId,
        activity.name,
        LatLng(activity.startPosition.lat, activity.startPosition.lon),
        "",
        icon,
        activity.activityType)
  }

  fun getActivityRatings(activity: Activity): String {
    return ""
  }
}
