package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.data.api.OutdoorActivityResponse
import com.lastaoutdoor.lasta.data.model.ActivityType
import com.lastaoutdoor.lasta.data.model.OutdoorActivity
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OutdoorActivityViewModel
@Inject
constructor(private val outdoorActivityRepository: OutdoorActivityRepository) : ViewModel() {
  fun getOutdoorActivities(): List<OutdoorActivity> {
    val climbingActivities = outdoorActivityRepository.getClimbingActivitiesNode(100, 46.519962, 6.633597)
    //create a list of OutdoorActivity objects from the climbingActivities
    val climbingActivitiesList = mutableListOf<OutdoorActivity>()
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
