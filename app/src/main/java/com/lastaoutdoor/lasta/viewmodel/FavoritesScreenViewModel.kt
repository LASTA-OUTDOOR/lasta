package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesScreenViewModel
@Inject
constructor(
    private val preferences: PreferencesRepository,
    private val activitiesDB: ActivitiesDBRepository
) : ViewModel() {
  private val _favoritesIds = MutableStateFlow<List<String>>(emptyList())
  val favoritesIds = _favoritesIds

  private val _favorites = MutableStateFlow<List<Activity>>(emptyList())
  val favorites = _favorites

  init {
    fetchFavorites()
  }

  private fun fetchFavorites() {
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
  }
}
