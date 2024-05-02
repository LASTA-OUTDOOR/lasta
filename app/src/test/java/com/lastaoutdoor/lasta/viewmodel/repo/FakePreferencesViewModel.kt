package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.models.user.UserPreferences
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class FakePreferencesRepository(override val userPreferencesFlow: Flow<UserPreferences>) :
    PreferencesRepository {

  override suspend fun updateIsLoggedIn(isLoggedIn: Boolean) {
    TODO("Not yet implemented")
  }

  override suspend fun updateUserInfo(userModel: UserModel) {
    TODO("Not yet implemented")
  }

  override suspend fun updateDescription(description: String) {
    TODO("Not yet implemented")
  }

  override suspend fun updateLanguage(language: Language) {
    TODO("Not yet implemented")
  }

  override suspend fun updatePrefActivity(activityType: ActivityType) {
    TODO("Not yet implemented")
  }

  override suspend fun updateActivityLevels(userActivitiesLevel: UserActivitiesLevel) {
    TODO("Not yet implemented")
  }

  override suspend fun updateClimbingLevel(level: UserLevel) {
    TODO("Not yet implemented")
  }

  override suspend fun updateHikingLevel(level: UserLevel) {
    TODO("Not yet implemented")
  }

  override suspend fun updateBikingLevel(level: UserLevel) {
    TODO("Not yet implemented")
  }

  override suspend fun updateFriends(friends: List<String>) {
    TODO("Not yet implemented")
  }

  override suspend fun updateFriendRequests(friendRequests: List<String>) {
    TODO("Not yet implemented")
  }

  override suspend fun updateFavorites(favorites: List<String>) {
    TODO("Not yet implemented")
  }

  override suspend fun updateDownloadedActivities(downloadedActivities: List<String>) {
    TODO("Not yet implemented")
  }

  override suspend fun clearPreferences() {
    TODO("Not yet implemented")
  }
}
