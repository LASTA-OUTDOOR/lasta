package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.models.user.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

  val userPreferencesFlow: Flow<UserPreferences>

  suspend fun updateIsLoggedIn(isLoggedIn: Boolean)

  suspend fun updateUserInfo(user: UserModel?)

  suspend fun updateHikingLevel(userLevel: UserLevel)

  suspend fun updateBio(bio: String)

  suspend fun clearPreferences()

  suspend fun updateLanguage(language: String)

  suspend fun updatePrefSport(prefSport: String)
}
