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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/** DataStore for storing user preferences and settings */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/** Enum class for hiking levels */
enum class HikingLevel(val level: Int) {
  BEGINNER(0),
  INTERMEDIATE(1),
  ADVANCED(2)
}

/** Class representing all possible local stored preferences */
data class UserPreferences(
    val isLoggedIn: Boolean,
    val uid: String,
    val userName: String,
    val email: String,
    val profilePictureUrl: String,
    val hikingLevel: HikingLevel
)

/**
 * DataStore class implementing methods for storing user preferences
 *
 * @param context the context of the application
 */
class PreferencesDataStore(private val context: Context) {
  private val dataStore = context.dataStore

  /**
   * Companion object containing all the keys for the preferences
   *
   * @property IS_LOGGED_IN_KEY key for the isLoggedIn preference
   * @property UID_KEY key for the uid preference
   * @property USER_NAME_KEY key for the userName preference
   * @property EMAIL_KEY key for the email preference
   * @property PROFILE_PICTURE_URL_KEY key for the profilePictureUrl preference
   * @property HIKING_LEVEL_KEY key for the hikingLevel preference
   */
  private companion object {
    val IS_LOGGED_IN_KEY = booleanPreferencesKey("isLoggedIn")
    val UID_KEY = stringPreferencesKey("uid")
    val USER_NAME_KEY = stringPreferencesKey("userName")
    val EMAIL_KEY = stringPreferencesKey("email")
    val PROFILE_PICTURE_URL_KEY = stringPreferencesKey("profilePictureUrl")
    val HIKING_LEVEL_KEY = stringPreferencesKey("hikingLevel")
  }

  /**
   * Flow of user preferences
   *
   * @return [Flow] of [UserPreferences] the flow of user preferences
   * @throws IOException if an I/O error occurs
   */
  val userPreferencesFlow: Flow<UserPreferences> =
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

  /**
   * Update the isLoggedIn preference
   *
   * @param isLoggedIn the new value for the isLoggedIn preference
   */
  suspend fun updateIsLoggedIn(isLoggedIn: Boolean) {
    dataStore.edit { preferences -> preferences[IS_LOGGED_IN_KEY] = isLoggedIn }
  }

  /**
   * Update the user information preferences
   *
   * @param uid the new value for the uid preference
   * @param userName the new value for the userName preference
   * @param email the new value for the email preference
   * @param profilePictureUrl the new value for the profilePictureUrl preference
   */
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

  /**
   * Update the hikingLevel preference
   *
   * @param hikingLevel the new value for the hikingLevel preference
   */
  suspend fun updateHikingLevel(hikingLevel: HikingLevel) {
    dataStore.edit { preferences -> preferences[HIKING_LEVEL_KEY] = hikingLevel.name }
  }
}
