package com.lastaoutdoor.lasta.ui.viewmodels

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class StatisticsViewModel @Inject constructor(private val repository: ActivitiesRepository) :
    ViewModel() {
  val totalTimeWalked = repository.getTotalTimeWalked()
}
