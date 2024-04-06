package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.data.model.Sports
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val repository: ActivitiesRepository) :
    ViewModel() {
  private var sport = mutableStateOf(Sports.HIKING)
  private val user = FirebaseAuth.getInstance().currentUser
  private val time = mutableStateOf("")

  suspend fun getChartXAxis() {
    if (user != null) {
      repository.getUserActivities(user, sport.value).run {}
    }
  }

  fun setSport(s: Sports) {
    sport.value = s
  }

  fun getSport() = sport.value
}
