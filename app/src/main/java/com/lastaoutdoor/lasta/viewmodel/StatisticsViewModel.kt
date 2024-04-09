package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.data.model.profile.Sports
import com.lastaoutdoor.lasta.data.model.profile.TimeFrame
import com.lastaoutdoor.lasta.data.model.profile.Trail
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import com.lastaoutdoor.lasta.utils.calculateTimeRange
import com.lastaoutdoor.lasta.utils.createDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val repository: ActivitiesRepository) :
    ViewModel() {
  private var sport = mutableStateOf(Sports.HIKING)
  private val user = FirebaseAuth.getInstance().currentUser
  private val time = mutableStateOf(TimeFrame.W)

  // Cache for storing fetched trails
  private var allTrailsCache: List<Trail> = emptyList()

  // Live data for filtered trails
  private val _trails = MutableStateFlow<List<Trail>>(emptyList())
  val trails: StateFlow<List<Trail>> = _trails

  init {
    fetchUserActivitiesOnce()
  }

  private fun fetchUserActivitiesOnce() {
    viewModelScope.launch {
      if (user != null) {
        allTrailsCache = repository.getUserActivities(user, sport.value)
        applyFilters()
      }
    }
  }

  private fun applyFilters() {
    _trails.value = filterTrailsByTimeFrame(allTrailsCache, time.value)
  }

  private fun filterTrailsByTimeFrame(trails: List<Trail>, timeFrame: TimeFrame): List<Trail> {
    val frame = calculateTimeRange(timeFrame)
    return trails.filter { trail ->
      val trailStart = Timestamp(trail.timeStarted)
      val trailEnd = Timestamp(trail.timeFinished)
      trailStart > frame.first && trailEnd < frame.second
    }
  }

  fun addTrailToUserActivities() {
    if (user != null) {
      repository.addTrailToUserActivities(
          user,
          Trail(
              1,
              5.0,
              200,
              null,
              6000,
              500,
              null,
              null,
              createDateTime(2024, 4, 7, 8, 0, 0),
              createDateTime(2024, 4, 7, 8, 30, 0)))

      repository.addTrailToUserActivities(
          user,
          Trail(
              2,
              6.0,
              400,
              null,
              11100,
              200,
              null,
              null,
              createDateTime(2024, 4, 8, 8, 0, 0),
              createDateTime(2024, 4, 8, 8, 30, 0)))

      repository.addTrailToUserActivities(
          user,
          Trail(
              3,
              2.0,
              100,
              null,
              12650,
              300,
              null,
              null,
              createDateTime(2024, 4, 5, 8, 0, 0),
              createDateTime(2024, 4, 5, 8, 30, 0)))
    }
  }

  fun setTimeFrame(timeFrame: TimeFrame) {
    time.value = timeFrame
  }

  fun getTimeFrame() = time.value

  fun setSport(s: Sports) {
    sport.value = s
  }

  fun getSport() = sport.value
}
