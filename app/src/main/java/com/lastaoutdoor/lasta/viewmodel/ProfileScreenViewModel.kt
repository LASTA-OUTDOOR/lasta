package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.data.db.ActivitiesRepositoryImpl
import com.lastaoutdoor.lasta.data.db.Trail
import com.lastaoutdoor.lasta.data.model.Sports
import com.lastaoutdoor.lasta.data.model.user_profile.TimeFrame
import com.lastaoutdoor.lasta.di.TimeProvider
import com.lastaoutdoor.lasta.utils.calculateTimeRangeUntilNow
import com.lastaoutdoor.lasta.utils.createDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileScreenViewModel
@Inject
constructor(
    private val repository: ActivitiesRepositoryImpl,
    private val timeProvider: TimeProvider
) : ViewModel() {

  private val user = FirebaseAuth.getInstance().currentUser

  // Cache for storing fetched trails
  private val _allTrailsCache = MutableStateFlow<List<Trail>>(emptyList())
  val allTrails = _allTrailsCache

  // Live data for filtered trails
  private val _trails = MutableStateFlow<List<Trail>>(emptyList())
  val trails: StateFlow<List<Trail>> = _trails

  private val _time = MutableStateFlow(TimeFrame.W)
  val timeFrame: StateFlow<TimeFrame> = _time

    private val _sport = MutableStateFlow(Sports.HIKING)
    val sport = _sport

  init {
    fetchUserActivities()
  }

  private fun fetchUserActivities() {
    viewModelScope.launch {
      if (user != null) {
        _allTrailsCache.value =
            repository.getUserActivities(user, sport.value).sortedBy { it.timeStarted }
        applyFilters()
      }
    }
  }

  private fun applyFilters() {
    _trails.value = filterTrailsByTimeFrame(_allTrailsCache.value, _time.value)
  }

  private fun filterTrailsByTimeFrame(trails: List<Trail>, timeFrame: TimeFrame): List<Trail> {
    return when (timeFrame) {
      TimeFrame.W,
      TimeFrame.M,
      TimeFrame.Y -> {
        val frame = calculateTimeRangeUntilNow(timeFrame, timeProvider)
        trails.filter { trail ->
          val trailStart = Timestamp(trail.timeStarted)
          val trailEnd = Timestamp(trail.timeFinished)
          trailStart > frame.first && trailEnd < frame.second
        }
      }
      TimeFrame.ALL -> trails
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
    _time.value = timeFrame
    applyFilters()
  }

  fun setSport(s: Sports) {
    _sport.value = s
    fetchUserActivities()
  }

}
