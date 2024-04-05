package com.lastaoutdoor.lasta.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class HikingLevel(val level: Int) {
  BEGINNER(0),
  INTERMEDIATE(1),
  ADVANCED(2)
}

data class UserPreferences(
    val isLoggedIn: Boolean,
    val uid: String,
    val userName: String,
    val email: String,
    val profilePictureUrl: String,
    val hikingLevel: HikingLevel
)

class PreferencesDataStore(private val context: Context) {
  private val dataStore = context.dataStore

  private companion object {
    val IS_LOGGED_IN_KEY = booleanPreferencesKey("isLoggedIn")
    val UID_KEY = stringPreferencesKey("uid")
    val USER_NAME_KEY = stringPreferencesKey("userName")
    val EMAIL_KEY = stringPreferencesKey("email")
    val PROFILE_PICTURE_URL_KEY = stringPreferencesKey("profilePictureUrl")
    val HIKING_LEVEL_KEY = stringPreferencesKey("hikingLevel")
  }

  val userPreferencesFlow =
      dataStore.data
          .catch { exception ->
            if (exception is IOException) {
              emit(emptyPreferences())
            } else {
              throw exception
            }
          }
          .map { preferences ->
            UserPreferences(
                isLoggedIn = preferences[IS_LOGGED_IN_KEY] ?: false,
                uid = preferences[UID_KEY] ?: "",
                userName = preferences[USER_NAME_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                profilePictureUrl = preferences[PROFILE_PICTURE_URL_KEY] ?: "",
                hikingLevel =
                    HikingLevel.valueOf(preferences[HIKING_LEVEL_KEY] ?: HikingLevel.BEGINNER.name))
          }

  suspend fun updateIsLoggedIn(isLoggedIn: Boolean) {
    dataStore.edit { preferences -> preferences[IS_LOGGED_IN_KEY] = isLoggedIn }
  }

  suspend fun updateUserInfo(
      uid: String,
      userName: String,
      email: String,
      profilePictureUrl: String
  ) {
    dataStore.edit { preferences ->
      preferences[UID_KEY] = uid
      preferences[USER_NAME_KEY] = userName
      preferences[EMAIL_KEY] = email
      preferences[PROFILE_PICTURE_URL_KEY] = profilePictureUrl
    }
  }

  suspend fun updateHikingLevel(hikingLevel: HikingLevel) {
    dataStore.edit { preferences -> preferences[HIKING_LEVEL_KEY] = hikingLevel.name }
  }
}
