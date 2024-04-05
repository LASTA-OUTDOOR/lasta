package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.repositories.ActivitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val repository: ActivitiesRepository) :
    ViewModel() {}
