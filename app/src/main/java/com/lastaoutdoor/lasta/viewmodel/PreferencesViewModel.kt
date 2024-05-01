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
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class PreferencesViewModel
@Inject
constructor(private val preferences: PreferencesRepository, private val userDB: UserDBRepository) :
    ViewModel() {
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
      preferences.updateDescription(description)
      userDB.updateField(userId.first(), "description", description)
    }
  }

  fun updateLanguage(language: Language) {
    viewModelScope.launch {
      preferences.updateLanguage(language)
      userDB.updateField(userId.first(), "language", language)
    }
  }

  fun updatePrefActivity(activityType: ActivityType) {
    viewModelScope.launch {
      preferences.updatePrefActivity(activityType)
      userDB.updateField(userId.first(), "prefActivity", activityType)
    }
  }

  fun updateActivityLevels(userActivitiesLevel: UserActivitiesLevel) {
    viewModelScope.launch {
      preferences.updateActivityLevels(userActivitiesLevel)
      userDB.updateField(userId.first(), "levels", userActivitiesLevel)
    }
  }

  fun updateClimbingLevel(level: UserLevel) {
    viewModelScope.launch { preferences.updateClimbingLevel(level) }
  }

  fun updateHikingLevel(level: UserLevel) {
    viewModelScope.launch { preferences.updateHikingLevel(level) }
  }

  fun updateBikingLevel(level: UserLevel) {
    viewModelScope.launch { preferences.updateBikingLevel(level) }
  }

  fun updateFriends(friends: List<String>) {
    viewModelScope.launch { preferences.updateFriends(friends) }
  }

  fun updateFriendRequests(friendRequests: List<String>) {
    viewModelScope.launch { preferences.updateFriendRequests(friendRequests) }
  }

  fun updateFavorites(favorites: List<String>) {
    viewModelScope.launch { preferences.updateFavorites(favorites) }
  }

  fun updateDownloadedActivities(downloadedActivities: List<String>) {
    viewModelScope.launch { preferences.updateDownloadedActivities(downloadedActivities) }
  }

  fun clearPreferences() {
    viewModelScope.launch { preferences.clearPreferences() }
  }
}
