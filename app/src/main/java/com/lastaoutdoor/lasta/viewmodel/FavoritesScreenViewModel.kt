package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.data.connectivity.ConnectivityRepositoryImpl
import com.lastaoutdoor.lasta.data.offline.ActivityDatabaseImpl
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesScreenViewModel
@Inject
constructor(
    private val preferences: PreferencesRepository,
    private val activitiesDB: ActivitiesDBRepository,
    private val offlineActivityDB: ActivityDatabaseImpl,
    private val connectivityRepositoryImpl: ConnectivityRepositoryImpl
) : ViewModel() {
  private val _isLoading = MutableStateFlow(true)
  val isLoading = _isLoading
  var isConnected =
      connectivityRepositoryImpl.connectionState.stateIn(
          initialValue = ConnectionState.OFFLINE,
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000))

  private val _favoritesIds = MutableStateFlow<List<String>>(emptyList())
  val favoritesIds = _favoritesIds

  private val _favorites = MutableStateFlow<List<Activity>>(emptyList())
  val favorites = _favorites

  init {

    if (isConnected.value == ConnectionState.CONNECTED) {
      fetchFavorites()
    } else {
      fetchOfflineFavorites()
    }
  }

  private fun fetchFavorites() {
    _isLoading.value = true
    viewModelScope.launch {
      preferences.userPreferencesFlow.collect { userPreferences ->
        val favoritesIds = userPreferences.user.favorites
        _favoritesIds.value = favoritesIds
        if (favoritesIds.isNotEmpty()) {
          val favorites = activitiesDB.getActivitiesByIds(favoritesIds)
          _favorites.value = favorites
        } else {
          _favorites.value = emptyList()
        }
      }
    }
    _isLoading.value = false
  }

  private fun fetchOfflineFavorites() {
    _isLoading.value = true
    viewModelScope.launch {
      val act = offlineActivityDB.getAllActivities()
      _favoritesIds.value = act.map { it.activityId }
      _favorites.value = act
    }

    _isLoading.value = false
  }
}
