package com.lastaoutdoor.lasta.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.models.user.UserPreferences
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
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
   * @property USERID_KEY key for the uid preference
   * @property USER_NAME_KEY key for the userName preference
   * @property EMAIL_KEY key for the email preference
   * @property PROFILE_PICTURE_URL_KEY key for the profilePictureUrl preference
   * @property HIKING_LEVEL_KEY key for the hikingLevel preference
   */
  private companion object {
    val IS_LOGGED_IN_KEY = booleanPreferencesKey("isLoggedIn")
    val USERID_KEY = stringPreferencesKey("uid")
    val USER_NAME_KEY = stringPreferencesKey("userName")
    val EMAIL_KEY = stringPreferencesKey("email")
    val PROFILE_PICTURE_URL_KEY = stringPreferencesKey("profilePictureUrl")
    val DESCRIPTION_KEY = stringPreferencesKey("description")
    val LANGUAGE_KEY = stringPreferencesKey("language")
    val PREF_ACTIVITY_KEY = stringPreferencesKey("prefActivity")
    val CLIMBING_LEVEL_KEY = stringPreferencesKey("climbingLevel")
    val HIKING_LEVEL_KEY = stringPreferencesKey("hikingLevel")
    val BIKING_LEVEL_KEY = stringPreferencesKey("bikingLevel")
    val FRIENDS_KEY = stringPreferencesKey("friends")
    val FRIEND_REQUESTS_KEY = stringPreferencesKey("friendRequests")
    val FAVORITES_KEY = stringPreferencesKey("favorites")
    val DOWNLOADED_ACTIVITIES_KEY = stringPreferencesKey("downloadedActivities")
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
                user =
                    UserModel(
                        userId = preferences[USERID_KEY] ?: "",
                        userName = preferences[USER_NAME_KEY] ?: "",
                        email = preferences[EMAIL_KEY] ?: "",
                        profilePictureUrl = preferences[PROFILE_PICTURE_URL_KEY] ?: "",
                        description = preferences[DESCRIPTION_KEY] ?: "",
                        language =
                            Language.valueOf(preferences[LANGUAGE_KEY] ?: Language.ENGLISH.name),
                        prefActivity =
                            ActivityType.valueOf(
                                preferences[PREF_ACTIVITY_KEY] ?: ActivityType.CLIMBING.name),
                        levels =
                            UserActivitiesLevel(
                                UserLevel.valueOf(
                                    preferences[CLIMBING_LEVEL_KEY] ?: UserLevel.BEGINNER.name),
                                UserLevel.valueOf(
                                    preferences[HIKING_LEVEL_KEY] ?: UserLevel.BEGINNER.name),
                                UserLevel.valueOf(
                                    preferences[BIKING_LEVEL_KEY] ?: UserLevel.BEGINNER.name)),
                        friends = preferences[FRIENDS_KEY]?.split(",") ?: emptyList(),
                        friendRequests =
                            preferences[FRIEND_REQUESTS_KEY]?.split(",") ?: emptyList(),
                        favorites = preferences[FAVORITES_KEY]?.split(",") ?: emptyList()),
                downloadedActivities =
                    preferences[DOWNLOADED_ACTIVITIES_KEY]?.split(",") ?: emptyList())
          }

  /**
   * Update the isLoggedIn preference
   *
   * @param isLoggedIn the new value for the isLoggedIn preference
   */
  override suspend fun updateIsLoggedIn(isLoggedIn: Boolean) {
    dataStore.edit { preferences -> preferences[IS_LOGGED_IN_KEY] = isLoggedIn }
  }

  override suspend fun updateUserInfo(userModel: UserModel) {
    dataStore.edit { preferences ->
      preferences[USERID_KEY] = userModel.userId
      preferences[USER_NAME_KEY] = userModel.userName
      preferences[EMAIL_KEY] = userModel.email
      preferences[PROFILE_PICTURE_URL_KEY] = userModel.profilePictureUrl
      preferences[DESCRIPTION_KEY] = userModel.description
      preferences[LANGUAGE_KEY] = userModel.language.name
      preferences[PREF_ACTIVITY_KEY] = userModel.prefActivity.name
      preferences[CLIMBING_LEVEL_KEY] = userModel.levels.climbingLevel.name
      preferences[HIKING_LEVEL_KEY] = userModel.levels.hikingLevel.name
      preferences[BIKING_LEVEL_KEY] = userModel.levels.bikingLevel.name
      preferences[FRIENDS_KEY] = userModel.friends.joinToString(",")
      preferences[FRIEND_REQUESTS_KEY] = userModel.friendRequests.joinToString(",")
      preferences[FAVORITES_KEY] = userModel.favorites.joinToString(",")
    }
  }

  override suspend fun updateDescription(description: String) {
    dataStore.edit { preferences -> preferences[DESCRIPTION_KEY] = description }
  }

  override suspend fun updateLanguage(language: Language) {
    dataStore.edit { preferences -> preferences[LANGUAGE_KEY] = language.name }
  }

  override suspend fun updatePrefActivity(activityType: ActivityType) {
    dataStore.edit { preferences -> preferences[PREF_ACTIVITY_KEY] = activityType.name }
  }

  override suspend fun updateActivityLevels(userActivitiesLevel: UserActivitiesLevel) {
    dataStore.edit { preferences ->
      preferences[CLIMBING_LEVEL_KEY] = userActivitiesLevel.climbingLevel.name
      preferences[HIKING_LEVEL_KEY] = userActivitiesLevel.hikingLevel.name
      preferences[BIKING_LEVEL_KEY] = userActivitiesLevel.bikingLevel.name
    }
  }

  override suspend fun updateClimbingLevel(level: UserLevel) {
    dataStore.edit { preferences -> preferences[CLIMBING_LEVEL_KEY] = level.name }
  }

  override suspend fun updateHikingLevel(level: UserLevel) {
    dataStore.edit { preferences -> preferences[HIKING_LEVEL_KEY] = level.name }
  }

  override suspend fun updateBikingLevel(level: UserLevel) {
    dataStore.edit { preferences -> preferences[BIKING_LEVEL_KEY] = level.name }
  }

  override suspend fun updateFriends(friends: List<String>) {
    dataStore.edit { preferences -> preferences[FRIENDS_KEY] = friends.joinToString(",") }
  }

  override suspend fun updateFriendRequests(friendRequests: List<String>) {
    dataStore.edit { preferences ->
      preferences[FRIEND_REQUESTS_KEY] = friendRequests.joinToString(",")
    }
  }

  override suspend fun updateFavorites(favorites: List<String>) {
    dataStore.edit { preferences -> preferences[FAVORITES_KEY] = favorites.joinToString(",") }
  }

  override suspend fun updateDownloadedActivities(downloadedActivities: List<String>) {
    dataStore.edit { preferences ->
      preferences[DOWNLOADED_ACTIVITIES_KEY] = downloadedActivities.joinToString(",")
    }
  }

  override suspend fun clearPreferences() {
    dataStore.edit { preferences -> preferences.clear() }
  }
}
