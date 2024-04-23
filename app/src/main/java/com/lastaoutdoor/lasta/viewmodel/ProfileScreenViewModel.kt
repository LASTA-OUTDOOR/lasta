package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.profile.TimeFrame
import com.lastaoutdoor.lasta.data.model.user.HikingLevel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.data.model.user.toUserModel
import com.lastaoutdoor.lasta.di.TimeProvider
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import com.lastaoutdoor.lasta.repository.PreferencesRepository
import com.lastaoutdoor.lasta.utils.calculateTimeRangeUntilNow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileScreenViewModel
@Inject
constructor(
    private val repository: ActivitiesRepository,
    private val timeProvider: TimeProvider,
    private val preferences: PreferencesRepository,
) : ViewModel() {

  private val _user = MutableStateFlow(UserModel("", "", "", "", HikingLevel.BEGINNER))
  val user = _user

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
    fetchCurrentUser()
    fetchUserActivities()
  }

  private fun fetchCurrentUser() {
    viewModelScope.launch {
      preferences.userPreferencesFlow.collect { userPreferences ->
        _user.value = userPreferences.toUserModel()
      }
    }
  }

  private fun fetchUserActivities() {
    viewModelScope.launch {
      _allTrailsCache.value =
          repository.getUserActivities(user.value, sport.value).sortedBy { it.timeStarted }
      applyFilters()
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

  fun setSport(s: ActivitiesDatabaseType.Sports) {
    _sport.value = s
    fetchUserActivities()
  }

  fun updateUser(user: UserModel) {
    _user.value = user
    fetchUserActivities()
  }
}
