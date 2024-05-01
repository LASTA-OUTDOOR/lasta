package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.map.ClimbingMarker
import com.lastaoutdoor.lasta.models.map.HikingMarker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class MoreInfoScreenViewModel @Inject constructor() : ViewModel() {
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
    activityToDisplay.value = activity
  }

  fun goToMarker(activity: Activity): com.lastaoutdoor.lasta.models.map.Marker {
    return when (activity.activityType) {
      ActivityType.CLIMBING ->
          ClimbingMarker(
              activity.name,
              LatLng(46.55, 6.549 /*activity.startPosition.lat,activity.startPosition.lon*/),
              "",
              R.drawable.climbing_icon)
      ActivityType.HIKING ->
          HikingMarker(
              activity.name,
              LatLng(activity.startPosition.lat, activity.startPosition.lon),
              "",
              R.drawable.hiking_icon,
              activity.osmId)
      ActivityType.BIKING ->
          HikingMarker(
              activity.name,
              LatLng(activity.startPosition.lat, activity.startPosition.lon),
              "",
              R.drawable.hiking_icon,
              activity.osmId,
              ActivityType.BIKING)
    }
  }

  fun switchMapIsDisplayed(bool: Boolean) {
    isMapDisplayed.value = bool
  }
}
