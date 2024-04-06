package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.data.model.Sports
import com.lastaoutdoor.lasta.data.model.Trail
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val repository: ActivitiesRepository) :
    ViewModel() {
  private var sport = mutableStateOf(Sports.HIKING)
  private val user = FirebaseAuth.getInstance().currentUser
  private val time = mutableStateOf("")

  private val _trails = MutableStateFlow<List<Trail>>(emptyList())
  val trails: StateFlow<List<Trail>> = _trails

  fun addTrailToUserActivities() {
    if (user != null) {
      repository.addTrailToUserActivities(
          user, Trail(1, 5.0, 200, null, 2000, 500, null, null, Date(), Date()))

      repository.addTrailToUserActivities(
          user, Trail(2, 6.0, 400, null, 5000, 200, null, null, Date(), Date()))

      repository.addTrailToUserActivities(
          user, Trail(3, 2.0, 100, null, 1000, 300, null, null, Date(), Date()))
    }
  }

  fun getTrailsFromUserActivities() {
    viewModelScope.launch {
      if (user != null) _trails.value = repository.getUserActivities(user, sport.value)
    }
  }

  fun setSport(s: Sports) {
    sport.value = s
  }

  fun getSport() = sport.value
}
