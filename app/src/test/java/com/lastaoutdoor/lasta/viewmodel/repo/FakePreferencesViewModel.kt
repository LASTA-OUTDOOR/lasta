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

  }

  override suspend fun updateUserInfo(userModel: UserModel) {

  }

  override suspend fun updateDescription(description: String) {

  }

  override suspend fun updateLanguage(language: Language) {

  }

  override suspend fun updatePrefActivity(activityType: ActivityType) {

  }

  override suspend fun updateActivityLevels(userActivitiesLevel: UserActivitiesLevel) {

  }

  override suspend fun updateClimbingLevel(level: UserLevel) {

  }

  override suspend fun updateHikingLevel(level: UserLevel) {

  }

  override suspend fun updateBikingLevel(level: UserLevel) {

  }

  override suspend fun updateFriends(friends: List<String>) {

  }

  override suspend fun updateFriendRequests(friendRequests: List<String>) {

  }

  override suspend fun updateFavorites(favorites: List<String>) {

  }

  override suspend fun updateDownloadedActivities(downloadedActivities: List<String>) {

  }

  override suspend fun clearPreferences() {

  }
}
