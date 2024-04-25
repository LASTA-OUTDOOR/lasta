package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.data.model.user.HikingLevel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel class for the Preferences data store
 *
 * @return [HiltViewModel] instance
 * @property preferences the [PreferencesDataStore] instance
 */
@HiltViewModel
class PreferencesViewModel @Inject constructor(private val preferences: PreferencesRepository) :
    ViewModel() {
  // Decompose UserPreferences into individual properties available as Flows
  val isLoggedIn = preferences.userPreferencesFlow.map { it.isLoggedIn }
  val userId = preferences.userPreferencesFlow.map { it.uid }
  val userName = preferences.userPreferencesFlow.map { it.userName }
  val email = preferences.userPreferencesFlow.map { it.email }
  val profilePictureUrl = preferences.userPreferencesFlow.map { it.profilePictureUrl }
  val hikingLevel = preferences.userPreferencesFlow.map { it.hikingLevel }
  val language = preferences.userPreferencesFlow.map { it.language }
  val prefSport = preferences.userPreferencesFlow.map { it.prefSport }

  /**
   * Updates the isLoggedIn preference
   *
   * @param isLoggedIn the new value for the preference
   */
  fun updateIsLoggedIn(isLoggedIn: Boolean) {
    viewModelScope.launch { preferences.updateIsLoggedIn(isLoggedIn) }
  }

  fun updateUserInfo(user: UserModel?) {
    viewModelScope.launch { preferences.updateUserInfo(user) }
  }

  /**
   * Updates the hiking level preference
   *
   * @param hikingLevel the new value for the hikingLevel preference
   */
  fun updateHikingLevel(hikingLevel: HikingLevel) {
    viewModelScope.launch { preferences.updateHikingLevel(hikingLevel) }
  }

  /**
   * Updates the language preference
   *
   * @param language the new value for the language preference
   */
  fun updateLanguage(language: String) {
    viewModelScope.launch { preferences.updateLanguage(language) }
  }

  /** Updates the prefSport preference */
  fun updatePrefSport(prefSport: String) {
    viewModelScope.launch { preferences.updatePrefSport(prefSport) }
  }

  /** Clears all preferences */
  fun clearPreferences() {
    viewModelScope.launch { preferences.clearPreferences() }
  }
}
