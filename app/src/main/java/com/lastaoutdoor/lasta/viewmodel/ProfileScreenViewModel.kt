package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.data.time.TimeProvider
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import com.lastaoutdoor.lasta.repository.db.UserActivitiesDBRepository
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import com.lastaoutdoor.lasta.utils.TimeFrame
import com.lastaoutdoor.lasta.utils.calculateTimeRangeUntilNow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for the ProfileScreen. It handles the business logic for the ProfileScreen.
 *
 * @property repository The repository to fetch user activities.
 * @property timeProvider The provider to get the current time.
 * @property preferences The repository to fetch user preferences.
 */
@HiltViewModel
class ProfileScreenViewModel
@Inject
constructor(
    private val repository: UserActivitiesDBRepository,
    private val timeProvider: TimeProvider,
    private val preferences: PreferencesRepository,
    private val userDBRepo: UserDBRepository
) : ViewModel() {

  /** The current user. */
  private val _user = MutableStateFlow(UserModel(""))
  val user = _user

  /** Flag to check if the current user is the logged in user. */
  private val _isCurrentUser = MutableStateFlow(true)
  val isCurrentUser = _isCurrentUser

  /** Cache for storing fetched activities. */
  private val _activitiesCache = MutableStateFlow<List<UserActivity>>(emptyList())
  val activities = _activitiesCache

  /** Live data for filtered activities. */
  private val _filteredActivities = MutableStateFlow<List<UserActivity>>(emptyList())
  val filteredActivities: StateFlow<List<UserActivity>> = _filteredActivities

  /** The selected time frame for filtering activities. */
  private val _time = MutableStateFlow(TimeFrame.W)
  val timeFrame: StateFlow<TimeFrame> = _time

  /** The selected sport for filtering activities. */
  private val _sport = MutableStateFlow(ActivityType.CLIMBING)
  val sport = _sport

  /** Initializes the ViewModel by fetching the current user and user activities. */
  init {
    fetchCurrentUser()
    //fetchUserActivities()
  }

  /** Fetches the current user from the preferences. */
  private fun fetchCurrentUser() {
    viewModelScope.launch {
      preferences.userPreferencesFlow.collect { userPreferences ->
        _user.value = userPreferences.user
      }
    }
  }

  /** Fetches the user activities from the repository. */
  private fun fetchUserActivities() {
    viewModelScope.launch {
      _activitiesCache.value =
          repository.getUserActivities(_user.value.userId).sortedBy { it.timeStarted }
      applyFilters()
    }
  }

  /** Applies the selected filters to the activities fetched from the repository. */
  private fun applyFilters() {
    _filteredActivities.value = filterTrailsByTimeFrame(_activitiesCache.value, _time.value)
  }

  /**
   * Filters the activities by the selected time frame.
   *
   * @param activities The list of activities to filter.
   * @param timeFrame The selected time frame.
   * @return The filtered list of activities.
   */
  private fun filterTrailsByTimeFrame(
      activities: List<UserActivity>,
      timeFrame: TimeFrame
  ): List<UserActivity> {
    return when (timeFrame) {
      TimeFrame.W,
      TimeFrame.M,
      TimeFrame.Y -> {
        val frame = calculateTimeRangeUntilNow(timeFrame, timeProvider)
        activities.filter { activity ->
          val trailStart = Timestamp(activity.timeStarted)
          val trailEnd = Timestamp(activity.timeFinished)
          trailStart > frame.first && trailEnd < frame.second
        }
      }
      TimeFrame.ALL -> activities
    }
  }

  /**
   * Sets the selected time frame and applies the filters.
   *
   * @param timeFrame The selected time frame.
   */
  fun setTimeFrame(timeFrame: TimeFrame) {
    _time.value = timeFrame
    applyFilters()
  }

  /**
   * Sets the selected sport and fetches the user activities.
   *
   * @param s The selected sport.
   */
  fun setSport(s: ActivityType) {
    _sport.value = s
    fetchUserActivities()
  }

  /**
   * Updates the current user and fetches the user activities.
   *
   * @param user The new user.
   */
  private fun updateUser(user: UserModel) {
    if (user != _user.value) {
      _user.value = user
      fetchUserActivities()
      viewModelScope.launch {
        _isCurrentUser.value = user.userId == preferences.userPreferencesFlow.first().user.userId
      }
    }
  }

  /**
   * Updates the current user by fetching the user from the database.
   *
   * @param uid The user id of the new user.
   */
  fun updateUser(userId: String) {
    viewModelScope.launch {
      val user = userDBRepo.getUserById(userId)
      if (user != null) updateUser(user)
    }
  }
}
