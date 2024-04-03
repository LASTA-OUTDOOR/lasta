package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.data.preferences.HikingLevel
import com.lastaoutdoor.lasta.data.preferences.PreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class PreferencesViewModel @Inject constructor(private val preferences: PreferencesDataStore) :
    ViewModel() {
  val isLoggedIn = preferences.userPreferencesFlow.map { it.isLoggedIn }
  val userId = preferences.userPreferencesFlow.map { it.uid }
  val userName = preferences.userPreferencesFlow.map { it.userName }
  val email = preferences.userPreferencesFlow.map { it.email }
  val profilePictureUrl = preferences.userPreferencesFlow.map { it.profilePictureUrl }
  val hikingLevel = preferences.userPreferencesFlow.map { it.hikingLevel }

  fun updateIsLoggedIn(isLoggedIn: Boolean) {
    viewModelScope.launch { preferences.updateIsLoggedIn(isLoggedIn) }
  }

  fun updateUserInfo(uid: String, userName: String, email: String, profilePictureUrl: String) {
    viewModelScope.launch { preferences.updateUserInfo(uid, userName, email, profilePictureUrl) }
  }

  fun updateHikingLevel(hikingLevel: HikingLevel) {
    viewModelScope.launch { preferences.updateHikingLevel(hikingLevel) }
  }
}
