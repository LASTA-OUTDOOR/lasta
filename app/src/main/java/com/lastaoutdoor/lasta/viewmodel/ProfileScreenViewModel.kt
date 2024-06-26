package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.data.offline.ActivityDatabaseImpl
import com.lastaoutdoor.lasta.data.time.TimeProvider
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.app.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import com.lastaoutdoor.lasta.repository.db.UserActivitiesDBRepository
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.utils.ErrorType
import com.lastaoutdoor.lasta.utils.TimeFrame
import com.lastaoutdoor.lasta.utils.filterTrailsByTimeFrame
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
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
    private val userDBRepo: UserDBRepository,
    private val errorToast: ErrorToast,
    private val connectivityRepositoryImpl: ConnectivityRepository,
    private val offlineActivityDB: ActivityDatabaseImpl,
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
  private val _isConnected =
      connectivityRepositoryImpl.connectionState.stateIn(
          initialValue = ConnectionState.OFFLINE,
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000))

  /** Initializes the ViewModel by fetching the current user and user activities. */
  init {
    fetchCurrentUser()
    fetchUserSingeSportActivities(sport.value)
    // addFakeActivity()
  }

  /** Fetches the current user from the preferences. */
  private fun fetchCurrentUser() {
    viewModelScope.launch {
      preferences.userPreferencesFlow.collect { userPreferences ->
        _user.value = userPreferences.user
      }
    }
  }

  fun addFakeActivity() {
    viewModelScope.launch {

      // Call surrounded by try-catch block to make handle exceptions caused by database
      try {
        repository.addUserActivity(
            "JGNE42k7GXapAwhUrdNwolWl41M2",
            ClimbingUserActivity(
                activityId = "pCrS5M9IqVbBBxug03rU",
                timeStarted =
                    Date.from(
                        LocalDate.of(2024, 5, 30)
                            .atTime(16, 32, 45)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()),
                timeFinished =
                    Date.from(
                        LocalDate.of(2024, 5, 30)
                            .atTime(17, 21, 45)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()),
                numPitches = 5,
                totalElevation = 449.3f))
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
  }

  /** Fetches the user activities from the repository. */
  private fun fetchUserActivities() {
    viewModelScope.launch {

      // Call surrounded by try-catch block to make handle exceptions caused by database
      try {
        _activitiesCache.value =
            repository.getUserActivities(_user.value.userId).sortedBy { it.timeStarted }
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
      applyFilters()
    }
  }

  private fun fetchUserSingeSportActivities(activityType: ActivityType) {
    viewModelScope.launch {
      // Check whether user is connected
      _isConnected.collect { cs ->
        if (cs == ConnectionState.CONNECTED) {
          try {
            // get activities from firebase db if connected
            when (activityType) {
              ActivityType.CLIMBING ->
                  _activitiesCache.value =
                      repository.getUserClimbingActivities(_user.value.userId).sortedBy {
                        it.timeStarted
                      }
              ActivityType.HIKING ->
                  _activitiesCache.value =
                      repository.getUserHikingActivities(_user.value.userId).sortedBy {
                        it.timeStarted
                      }
              ActivityType.BIKING ->
                  _activitiesCache.value =
                      repository.getUserBikingActivities(_user.value.userId).sortedBy {
                        it.timeStarted
                      }
            }
          } catch (e: Exception) {
            errorToast.showToast(ErrorType.ERROR_DATABASE)
          }
        } else {
          // get activities from room database if offline
          when (activityType) {
            ActivityType.CLIMBING ->
                _activitiesCache.value = offlineActivityDB.getClimbingActivities()
            ActivityType.HIKING -> _activitiesCache.value = offlineActivityDB.getHikingActivities()
            ActivityType.BIKING -> _activitiesCache.value = offlineActivityDB.getBikingActivities()
          }
        }
        applyFilters()
      }
      // Call surrounded by try-catch block to make handle exceptions caused by database

    }
  }

  /** Applies the selected filters to the activities fetched from the repository. */
  private fun applyFilters() {
    _filteredActivities.value =
        filterTrailsByTimeFrame(_activitiesCache.value, _time.value, timeProvider)
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
    fetchUserSingeSportActivities(s)
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
   * @param userId The user id of the new user.
   */
  fun updateUser(userId: String) {
    viewModelScope.launch {

      // Call surrounded by try-catch block to make handle exceptions caused by database
      try {
        val user = userDBRepo.getUserById(userId)
        if (user != null) {
          updateUser(user)
        }
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
  }
}
