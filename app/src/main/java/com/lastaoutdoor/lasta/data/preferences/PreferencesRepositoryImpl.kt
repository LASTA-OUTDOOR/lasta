package com.lastaoutdoor.lasta.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lastaoutdoor.lasta.data.model.user.UserLevel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.data.model.user.UserPreferences
import com.lastaoutdoor.lasta.repository.PreferencesRepository
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/** DataStore for storing user preferences and settings */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * DataStore class implementing methods for storing user preferences
 *
 * @param context the context of the application
 */
class PreferencesRepositoryImpl(private val context: Context) : PreferencesRepository {
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
    val BIO_KEY = stringPreferencesKey("bio")
    val HIKING_LEVEL_KEY = stringPreferencesKey("hikingLevel")
    val LANGUAGE_KEY = stringPreferencesKey("language")
    val PREF_SPORT_KEY = stringPreferencesKey("prefSport")
  }

  /**
   * Flow of user preferences
   *
   * @return [Flow] of [UserPreferences] the flow of user preferences
   * @throws IOException if an I/O error occurs
   */
  override val userPreferencesFlow: Flow<UserPreferences> =
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
                bio = preferences[BIO_KEY] ?: "",
                userLevel =
                    UserLevel.valueOf(preferences[HIKING_LEVEL_KEY] ?: UserLevel.BEGINNER.name),
                language = preferences[LANGUAGE_KEY] ?: "",
                prefSport = preferences[PREF_SPORT_KEY] ?: "")
          }

  /**
   * Update the isLoggedIn preference
   *
   * @param isLoggedIn the new value for the isLoggedIn preference
   */
  override suspend fun updateIsLoggedIn(isLoggedIn: Boolean) {
    dataStore.edit { preferences -> preferences[IS_LOGGED_IN_KEY] = isLoggedIn }
  }

  override suspend fun updateUserInfo(user: UserModel?) {
    dataStore.edit { preferences ->
      preferences[UID_KEY] = user?.userId ?: ""
      preferences[USER_NAME_KEY] = user?.userName ?: ""
      preferences[EMAIL_KEY] = user?.email ?: ""
      preferences[PROFILE_PICTURE_URL_KEY] = user?.profilePictureUrl ?: ""
      preferences[BIO_KEY] = user?.bio ?: ""
    }
  }

  /**
   * Update the hikingLevel preference
   *
   * @param userLevel the new value for the hikingLevel preference
   */
  override suspend fun updateHikingLevel(userLevel: UserLevel) {
    dataStore.edit { preferences -> preferences[HIKING_LEVEL_KEY] = userLevel.name }
  }

  override suspend fun updateLanguage(language: String) {
    dataStore.edit { preferences -> preferences[LANGUAGE_KEY] = language }
  }

  override suspend fun updatePrefSport(prefSport: String) {
    dataStore.edit { preferences -> preferences[PREF_SPORT_KEY] = prefSport }
  }

  override suspend fun updateBio(bio: String) {
    dataStore.edit { preferences -> preferences[BIO_KEY] = bio }
  }

  override suspend fun clearPreferences() {
    dataStore.edit { preferences -> preferences.clear() }
  }
}
