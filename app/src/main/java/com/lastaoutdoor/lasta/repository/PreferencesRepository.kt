package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.data.preferences.HikingLevel
import com.lastaoutdoor.lasta.data.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateIsLoggedIn(isLoggedIn: Boolean)

    suspend fun updateUserInfo(uid: String, userName: String, email: String, profilePictureUrl: String)

    suspend fun updateHikingLevel(hikingLevel: HikingLevel)

}