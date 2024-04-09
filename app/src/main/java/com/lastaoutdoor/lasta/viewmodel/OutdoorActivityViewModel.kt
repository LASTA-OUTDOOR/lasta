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
    return climbingActivities.elements
  }

  fun refresh() {}

  fun filter() {}
}
