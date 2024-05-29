package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.offline.ActivityDatabaseImpl
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.models.api.NodeWay
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.models.api.Relation
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.repository.app.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.utils.ErrorType
import com.lastaoutdoor.lasta.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MoreInfoScreenViewModel
@Inject
constructor(
    private val activityRepository: ActivityRepository,
    private val activityDB: ActivitiesDBRepository,
    private val activitydaoImpl: ActivityDatabaseImpl,
    private val userDB: UserDBRepository,
    private val errorToast: ErrorToast,
    private val connectivityRepositoryImpl: ConnectivityRepository,
) : ViewModel() {
  val isConnected =
      connectivityRepositoryImpl.connectionState.stateIn(
          initialValue = ConnectionState.OFFLINE,
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000))
  /* Just a default activity to fill in the mutable state*/
  private val dummyActivity = Activity("", 0, ActivityType.CLIMBING, "Dummy")
  val activityToDisplay = mutableStateOf(dummyActivity)

  private val _isMapDisplayed = MutableStateFlow(false)
  val isMapDisplayed = _isMapDisplayed

  private val _usersList = MutableStateFlow<ArrayList<UserModel?>>(arrayListOf())
  val usersList = _usersList

  private val _ratings = MutableStateFlow<ArrayList<Rating>>(arrayListOf())
  val ratings = _ratings

  /*Changes the int difficulty of the activity for its String equivalent : 0 -> Easy, 1 -> Medium, etc...*/
  fun processDiffText(activity: Activity): String {
    return when (activity.difficulty) {
      Difficulty.EASY -> "Easy"
      Difficulty.NORMAL -> "Normal"
      Difficulty.HARD -> "Hard"
    }
  }

  fun changeActivityToDisplay(activity: Activity) {
    viewModelScope.launch {
      isConnected.collect {
        if (it == ConnectionState.CONNECTED) {
          // We get the start activity position of the activity from the OSM API
          when (val response =
              when (activity.activityType) {
                ActivityType.CLIMBING -> activityRepository.getClimbingPointById(activity.osmId)
                ActivityType.HIKING -> activityRepository.getHikingRouteById(activity.osmId)
                ActivityType.BIKING -> activityRepository.getBikingRouteById(activity.osmId)
              }) {
            is Response.Loading -> {}
            is Response.Success -> {
              val osmData = response.data!!
              var newPos = osmData.getPosition()

              // If there's a problem with the OSM API data for the starting position,
              // we try to get it from its itinerary
              if (newPos == Position(0.0, 0.0)) {
                newPos =
                    when (activity.activityType) {
                      ActivityType.HIKING,
                      ActivityType.BIKING -> {
                        (osmData as Relation)
                            .ways
                            ?.flatMap { it.nodes ?: emptyList() }
                            ?.firstOrNull { it.lat != 0.0 && it.lon != 0.0 } ?: Position(0.0, 0.0)
                      }
                      ActivityType.CLIMBING -> (osmData as NodeWay).center!!
                    }
              }

              // Update the activity with the new start position
              val updatedActivity = activity.copy(startPosition = newPos)

              // Call surrounded by try-catch block to make handle exceptions caused by database
              try {
                activityDB.updateStartPosition(activity.activityId, osmData.getPosition())
                activityToDisplay.value = updatedActivity
              } catch (e: Exception) {
                errorToast.showToast(ErrorType.ERROR_DATABASE)
              }
            }
            is Response.Failure -> {
              // Handle OSM API exceptions
              errorToast.showToast(ErrorType.ERROR_OSM_API)
            }
          }
        } else {
          activityToDisplay.value = activity
        }
      }
    }
  }

  /*Change activity to display from the activity ID */
  fun changeActivityToDisplayByID(activityId: String) {
    viewModelScope.launch {
      // Call surrounded by try-catch block to make handle exceptions caused by database
      try {
        val activityToDisplay = activityDB.getActivityById(activityId) ?: dummyActivity
        changeActivityToDisplay(activityToDisplay)
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
  }

  // Update the activity's difficulty
  fun updateDifficulty(activityId: String) {
    viewModelScope.launch {
      // Call surrounded by try-catch block to make handle exceptions caused by database
      try {
        // update the difficulty in a cyclic way
        activityDB.updateDifficulty(activityId)
        activityToDisplay.value = activityDB.getActivityById(activityId) ?: dummyActivity
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
  }

  fun goToMarker(activity: Activity): Marker {
    val icon =
        when (activity.activityType) {
          ActivityType.CLIMBING -> R.drawable.climbing_icon
          ActivityType.HIKING -> R.drawable.hiking_icon
          ActivityType.BIKING -> R.drawable.biking_icon
        }
    return Marker(
        activity.osmId,
        activity.name,
        LatLng(activity.startPosition.lat, activity.startPosition.lon),
        "",
        icon,
        activity.activityType)
  }

  fun downloadActivity(a: Activity) {
    viewModelScope.launch {
      // Call surrounded by try-catch block to make handle exceptions caused by the DAO
      try {
        activitydaoImpl.insertActivity(a)
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_DAO)
      }
    }
  }

  fun getUserModels(userIds: List<String>) {
    viewModelScope.launch {
      val users = ArrayList<UserModel?>()

      // Call surrounded by try-catch block to make handle exceptions caused by database
      try {
        for (userId in userIds) {
          users.add(userDB.getUserById(userId))
        }
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
      _usersList.value = users
    }
  }

  fun writeNewRating(activityId: String, rating: Rating, newMeanRating: String) {
    viewModelScope.launch {

      // Call surrounded by try-catch block to make handle exceptions caused by database
      try {
        activityDB.addRating(activityId, rating, newMeanRating)
        activityToDisplay.value = activityDB.getActivityById(activityId) ?: dummyActivity
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
    _ratings.value.add(rating)
  }
}
