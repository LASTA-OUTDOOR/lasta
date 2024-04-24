package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.data.model.user.UserLevel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.data.model.user.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

  val userPreferencesFlow: Flow<UserPreferences>

  suspend fun updateIsLoggedIn(isLoggedIn: Boolean)

  suspend fun updateUserInfo(user: UserModel?)

  suspend fun updateHikingLevel(userLevel: UserLevel)

  suspend fun clearPreferences()
}
