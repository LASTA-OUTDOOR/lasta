package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OutdoorActivityViewModel
@Inject
constructor(private val outdoorActivityRepository: OutdoorActivityRepository) : ViewModel() {
  fun getOutdoorActivities() {
    outdoorActivityRepository.getClimbingActivitiesNode(1000, 52.5200, 13.4050)
    outdoorActivityRepository.getClimbingActivitiesWay(1000, 52.5200, 13.4050)
    outdoorActivityRepository.getHikingActivities(1000, 52.5200, 13.4050)
  }

  fun refresh() {}

  fun filter() {}
}
