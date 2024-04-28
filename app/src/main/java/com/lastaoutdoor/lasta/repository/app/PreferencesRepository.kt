package com.lastaoutdoor.lasta.repository.app

import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.models.user.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

  val userPreferencesFlow: Flow<UserPreferences>

  suspend fun updateIsLoggedIn(isLoggedIn: Boolean)

  suspend fun updateUserInfo(userModel: UserModel)

  suspend fun updateDescription(description: String)

  suspend fun updateLanguage(language: Language)

  suspend fun updatePrefActivity(activityType: ActivityType)

  suspend fun updateActivityLevels(userActivitiesLevel: UserActivitiesLevel)

  suspend fun updateClimbingLevel(level: UserLevel)

  suspend fun updateHikingLevel(level: UserLevel)

  suspend fun updateBikingLevel(level: UserLevel)

  suspend fun clearPreferences()
}
