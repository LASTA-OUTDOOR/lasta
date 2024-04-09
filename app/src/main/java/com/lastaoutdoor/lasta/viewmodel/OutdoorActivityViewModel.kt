package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.data.model.ActivityType
import com.lastaoutdoor.lasta.data.model.OutdoorActivity
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OutdoorActivityViewModel
@Inject
constructor(private val outdoorActivityRepository: OutdoorActivityRepository) : ViewModel() {

  /**
   * fetches outdoor activities, for now being only climbing activities since hiking activities are
   * imprecise and incomplete
   */
  fun getOutdoorActivities(): List<OutdoorActivity> {
    val climbingActivities =
        outdoorActivityRepository.getClimbingActivitiesNode(10000, 46.519962, 6.633597)
    // create a list of OutdoorActivity objects from the climbingActivities
    val climbingActivitiesList = mutableListOf<OutdoorActivity>()
    // add each climbing activity to the list
    climbingActivities.elements.forEach {
      climbingActivitiesList.add(
          OutdoorActivity(
              ActivityType.CLIMBING,
              it.difficulty,
              it.length,
              it.duration,
              it.locationName,
          ))
    }
    return climbingActivitiesList
  }

  fun refresh() {}

  fun filter() {}
}
