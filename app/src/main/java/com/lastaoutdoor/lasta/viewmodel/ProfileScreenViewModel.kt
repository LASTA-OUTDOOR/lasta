package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.profile.TimeFrame
import com.lastaoutdoor.lasta.di.TimeProvider
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import com.lastaoutdoor.lasta.utils.calculateTimeRangeUntilNow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileScreenViewModel
@Inject
constructor(private val repository: ActivitiesRepository, private val timeProvider: TimeProvider) :
    ViewModel() {

  // TODO : MAKE IT WITH UserModel
  private val user = FirebaseAuth.getInstance().currentUser
  private val time = mutableStateOf(TimeFrame.W)

  // Cache for storing fetched trails
  private val _allTrailsCache = MutableStateFlow<List<ActivitiesDatabaseType>>(emptyList())
  val allTrails = _allTrailsCache

  // Live data for filtered trails
  private val _trails = MutableStateFlow<List<ActivitiesDatabaseType>>(emptyList())
  val trails: StateFlow<List<ActivitiesDatabaseType>> = _trails

  private val _time = MutableStateFlow(TimeFrame.W)
  val timeFrame: StateFlow<TimeFrame> = _time

  private val _sport = MutableStateFlow(ActivitiesDatabaseType.Sports.HIKING)
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

  private fun filterTrailsByTimeFrame(
      trails: List<ActivitiesDatabaseType>,
      timeFrame: TimeFrame
  ): List<ActivitiesDatabaseType> {
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

  fun setTimeFrame(timeFrame: TimeFrame) {
    _time.value = timeFrame
    applyFilters()
  }

  fun getTimeFrame() = time.value

  fun setSport(s: ActivitiesDatabaseType.Sports) {
    _sport.value = s
    fetchUserActivities()
  }

  fun getSport() = sport.value
}