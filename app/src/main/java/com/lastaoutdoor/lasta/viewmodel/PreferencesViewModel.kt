package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.utils.ErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class PreferencesViewModel
@Inject
constructor(
    private val preferences: PreferencesRepository,
    private val userDB: UserDBRepository,
    private val errorToast: ErrorToast
) : ViewModel() {
  // Decompose UserPreferences into individual properties available as Flows
  val isLoggedIn = preferences.userPreferencesFlow.map { it.isLoggedIn }.asLiveData()
  val user = preferences.userPreferencesFlow.map { it.user }
  val userId = preferences.userPreferencesFlow.map { it.user.userId }
  val userName = preferences.userPreferencesFlow.map { it.user.userName }
  val email = preferences.userPreferencesFlow.map { it.user.userName }
  val profilePictureUrl = preferences.userPreferencesFlow.map { it.user.profilePictureUrl }
  val description = preferences.userPreferencesFlow.map { it.user.description }
  val levels = preferences.userPreferencesFlow.map { it.user.levels }
  val language = preferences.userPreferencesFlow.map { it.user.language }
  val prefActivity = preferences.userPreferencesFlow.map { it.user.prefActivity }
  val friends = preferences.userPreferencesFlow.map { it.user.friends }
  val friendRequests = preferences.userPreferencesFlow.map { it.user.friendRequests }
  val favorites = preferences.userPreferencesFlow.map { it.user.favorites }
  val downloadedActivities = preferences.userPreferencesFlow.map { it.downloadedActivities }

  /**
   * Updates the isLoggedIn preference
   *
   * @param isLoggedIn the new value for the preference
   */
  fun updateIsLoggedIn(isLoggedIn: Boolean) {
    viewModelScope.launch { preferences.updateIsLoggedIn(isLoggedIn) }
  }

  fun updateUserInfo(user: UserModel) {
    viewModelScope.launch { preferences.updateUserInfo(user) }
  }

  fun updateDescription(description: String) {
    viewModelScope.launch {
      try {
        userDB.updateField(userId.first(), "description", description)
        preferences.updateDescription(description)
      } catch (e: Exception) {
        e.printStackTrace()
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
  }

  fun updateLanguage(language: Language) {
    viewModelScope.launch {
      try {
        userDB.updateField(userId.first(), "language", language)
        preferences.updateLanguage(language)
      } catch (e: Exception) {
        e.printStackTrace()
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
  }

  fun updatePrefActivity(activityType: ActivityType) {
    viewModelScope.launch {
      try {
        userDB.updateField(userId.first(), "prefActivity", activityType)
        preferences.updatePrefActivity(activityType)
      } catch (e: Exception) {
        e.printStackTrace()
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
  }

  fun updateActivityLevels(userActivitiesLevel: UserActivitiesLevel) {
    viewModelScope.launch {
      try {
        userDB.updateField(userId.first(), "levels", userActivitiesLevel)
        preferences.updateActivityLevels(userActivitiesLevel)
      } catch (e: Exception) {
        e.printStackTrace()
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
  }

  fun updateClimbingLevel(level: UserLevel) {
    viewModelScope.launch {
      try {
        userDB.updateField(
            userId.first(),
            "levels",
            hashMapOf(
                "climbingLevel" to level.name,
                "hikingLevel" to levels.first().hikingLevel.name,
                "bikingLevel" to levels.first().bikingLevel.name))
        preferences.updateClimbingLevel(level)
      } catch (e: Exception) {
        e.printStackTrace()
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
  }

  fun updateHikingLevel(level: UserLevel) {
    viewModelScope.launch {
      try {
        userDB.updateField(
            userId.first(),
            "levels",
            hashMapOf(
                "climbingLevel" to levels.first().climbingLevel.name,
                "hikingLevel" to level.name,
                "bikingLevel" to levels.first().bikingLevel.name))
        preferences.updateHikingLevel(level)
      } catch (e: Exception) {
        e.printStackTrace()
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
  }

  fun updateBikingLevel(level: UserLevel) {
    viewModelScope.launch {
      try {
        userDB.updateField(
            userId.first(),
            "levels",
            hashMapOf(
                "climbingLevel" to levels.first().climbingLevel.name,
                "hikingLevel" to levels.first().hikingLevel.name,
                "bikingLevel" to level.name))
        preferences.updateBikingLevel(level)
      } catch (e: Exception) {
        e.printStackTrace()
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
  }

  fun updateFriends(friends: List<String>) {
    viewModelScope.launch { preferences.updateFriends(friends) }
  }

  fun updateFriendRequests(friendRequests: List<String>) {
    viewModelScope.launch { preferences.updateFriendRequests(friendRequests) }
  }

  fun flipFavorite(favoriteId: String) {
    viewModelScope.launch {
      if (favorites.first().contains(favoriteId)) {
        try {
          val newFavorites = favorites.first().filter { it.isNotEmpty() }.toMutableList()
          userDB.removeFavorite(userId.first(), favoriteId)
          newFavorites.remove(favoriteId)
          preferences.updateFavorites(newFavorites)
        } catch (e: Exception) {
          e.printStackTrace()
          errorToast.showToast(ErrorType.ERROR_DATABASE)
        }
      } else {
        try {
          val newFavorites = favorites.first().filter { it.isNotEmpty() }.toMutableList()
          userDB.addFavorite(userId.first(), favoriteId)
          newFavorites.add(favoriteId)
          preferences.updateFavorites(newFavorites)
        } catch (e: Exception) {
          e.printStackTrace()
          errorToast.showToast(ErrorType.ERROR_DATABASE)
        }
      }
    }
  }

  fun updateDownloadedActivities(downloadedActivities: List<String>) {
    viewModelScope.launch { preferences.updateDownloadedActivities(downloadedActivities) }
  }

  fun clearPreferences() {
    viewModelScope.launch { preferences.clearPreferences() }
  }
}
