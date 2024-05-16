package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.data.offline.ActivityDatabaseImpl
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.repository.app.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.utils.ErrorType
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
    private val connectivityRepositoryImpl: ConnectivityRepository,
    private val errorToast: ErrorToast
) : ViewModel() {
  private val _isLoading = MutableStateFlow(true)
  val isLoading = _isLoading
  private val _isConnected =
      connectivityRepositoryImpl.connectionState.stateIn(
          initialValue = ConnectionState.OFFLINE,
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000))

  private val _favoritesIds = MutableStateFlow<List<String>>(emptyList())
  val favoritesIds = _favoritesIds

  private val _favorites = MutableStateFlow<List<Activity>>(emptyList())
  val favorites = _favorites

  init {
    fetchFavorites()
  }

  private fun fetchFavorites() {
    _isLoading.value = true

    viewModelScope.launch {
      _isConnected.collect {
        when (it) {
          ConnectionState.CONNECTED -> {
            preferences.userPreferencesFlow.collect { userPreferences ->
              val favoritesIds = userPreferences.user.favorites
              _favoritesIds.value = favoritesIds
              if (favoritesIds.isNotEmpty()) {
                try {
                  val favorites = activitiesDB.getActivitiesByIds(favoritesIds)
                  _favorites.value = favorites
                } catch (e: Exception) {
                  e.printStackTrace()
                  errorToast.showToast(ErrorType.ERROR_DATABASE)
                }
              } else {
                _favorites.value = emptyList()
              }
            }
          }
          ConnectionState.OFFLINE -> {
            try {
              val act = offlineActivityDB.getAllActivities()
              _favoritesIds.value = act.map { it.activityId }
              _favorites.value = act
            } catch (e: Exception) {
              e.printStackTrace()
              errorToast.showToast(ErrorType.ERROR_DATABASE)
            }
          }
        }
      }
    }
    _isLoading.value = false
  }
}
